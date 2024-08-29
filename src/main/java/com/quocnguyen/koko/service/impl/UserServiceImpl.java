package com.quocnguyen.koko.service.impl;

import com.quocnguyen.koko.dto.AppPaging;
import com.quocnguyen.koko.dto.UserContactDTO;
import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.dto.UserFriendDTO;
import com.quocnguyen.koko.event.FriendAcceptEvent;
import com.quocnguyen.koko.event.FriendRequestEvent;
import com.quocnguyen.koko.exception.ResourceNotFoundException;
import com.quocnguyen.koko.model.*;
import com.quocnguyen.koko.repository.RelationshipRepository;
import com.quocnguyen.koko.repository.UserRepository;
import com.quocnguyen.koko.repository.VerificationCodeRepository;
import com.quocnguyen.koko.service.UserService;
import com.quocnguyen.koko.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * @author Quoc Nguyen on {7/31/2024}
 */

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final VerificationCodeRepository vcRepo;
    private final RelationshipRepository relationshipRepo;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public UserDetails loadByUsername(String username) {
        return userRepo.findByUsername(username)
                .map(AppUserDetails::new)
                .orElse(null);

    }

    @Override
    public UserDTO verifyAccount(String token) {
        UserDetails userDetails = getUserFromCxt();

        User user = userRepo
                .findByUsername(userDetails.getUsername())
                .orElse(null);
        Assert.notNull(user, "The user must not null");

        if(!user.isVerified()) {
            VerificationCode verificationCode = vcRepo
                    .findByUserIdAAndCodeType(user.getId(), VerificationCode.CodeType.VERIFICATION_EMAIL)
                    .orElse(null);

            if(verificationCode == null)
                return null;

            if(verificationCode.getExpiresAt().before(new Date(System.currentTimeMillis()))) {
                vcRepo.delete(verificationCode);
                return null;
            }

            if(!verificationCode.getToken().equals(token)) {
                if(verificationCode.getNumTrial() <= 1) {
                    vcRepo.delete(verificationCode);
                } else {
                    verificationCode.setNumTrial(verificationCode.getNumTrial() - 1);
                    vcRepo.save(verificationCode);
                }

                return null;
            }

            vcRepo.delete(verificationCode);
            user.setVerified(true);
            userRepo.save(user);
        }

        UserDTO returnObj = new UserDTO();
        BeanUtils.copyProperties(user, returnObj);

        return returnObj;
    }

    private UserDetails getUserFromCxt() {
        return (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Override
    public UserDTO getAuthenticatedUser() {
        UserDetails userDetails = getUserFromCxt();

        User user = userRepo.findByUsername(userDetails.getUsername()).orElse(null);

        Assert.notNull(user, "User from context must not null in database");

        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);

        return dto;
    }

    @Override
    public AppPaging<UserContactDTO> getFriends(String keyword, int pageNum, int pageSize) {
        var user = getAuthenticatedUser();
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<Relationship> relationships = relationshipRepo.getFriends(user.getId(), keyword, pageable);

        List<UserContactDTO> contacts = relationships
                .getContent()
                .stream()
                .map((rel) -> {
                    var friend = rel.getRelatedUser();
                    return UserContactDTO
                            .builder()
                            .id(friend.getId())
                            .username(friend.getUsername())
                            .avatar(friend.getAvatar())
                            .name(friend.getName())
                            .friendStatus(UserContactDTO.FriendStatus.FRIEND)
                            .build();
                })
                .toList();

        AppPaging<UserContactDTO> paging = AppPaging.convertExcludeContent(relationships);
        paging.setList(contacts);

        return paging;
    }

    @Override
    public UserContactDTO checkFriendStatus(Long friendId) {
        var user = getAuthenticatedUser();

        User friend = userRepo.findById(friendId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        var rel1 = relationshipRepo.findById(new RelationshipId(user.getId(), friendId)).orElse(null);
        var rel2 = relationshipRepo.findById(new RelationshipId(friendId, user.getId())).orElse(null);

        var res = UserContactDTO
                .builder()
                .id(friend.getId())
                .username(friend.getUsername())
                .name(friend.getName())
                .build();

        if(rel1 != null && rel2 != null)
            res.setFriendStatus(UserContactDTO.FriendStatus.FRIEND);
        else if(rel1 != null)
            res.setFriendStatus(UserContactDTO.FriendStatus.SENT_REQUEST);
        else if(rel2 != null)
            res.setFriendStatus(UserContactDTO.FriendStatus.RECEIVED_REQUEST);
        else
            res.setFriendStatus(UserContactDTO.FriendStatus.STRANGER);

        return res;
    }

    @Override
    public UserContactDTO requestFriend(Long friendId) {
        var user = getAuthenticatedUser();
        var friend = userRepo.findById(friendId).orElse(null);

        if(friend == null)
            return null;

        var rel1 = relationshipRepo.findById(new RelationshipId(user.getId(), friendId)).orElse(null);
        var rel2 = relationshipRepo.findById(new RelationshipId(friendId, user.getId())).orElse(null);
        if(rel1 != null || rel2 != null)
            return null;

        Relationship relationship = Relationship
                .builder()
                .user(new User(user.getId()))
                .relatedUser(friend)
                .id(new RelationshipId(user.getId(), friendId))
                .type(Relationship.RelationshipType.FRIEND)
                .createdAt(new Date())
                .build();

        relationship = relationshipRepo.save(relationship);
        eventPublisher.publishEvent(new FriendRequestEvent(this, relationship));

        var res = UserContactDTO
                .builder()
                .id(friend.getId())
                .username(friend.getUsername())
                .name(friend.getName())
                .friendStatus(UserContactDTO.FriendStatus.SENT_REQUEST)
                .build();

        return res;
    }

    @Override
    public AppPaging<UserFriendDTO> getFriendRequests(int pageNum, int pageSize) {
        var user = getAuthenticatedUser();
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        var page = relationshipRepo.getFriendRequests(user.getId(), pageable);

        AppPaging<UserFriendDTO> paging = AppPaging.convertExcludeContent(page);

        paging.setList(
                page.getContent()
                        .stream()
                        .map(rel -> {
                            return UserFriendDTO.builder()
                                    .relatedUser(
                                            UserContactDTO.builder()
                                                    .id(rel.getUser().getId())
                                                    .username(rel.getUser().getUsername())
                                                    .name(rel.getUser().getName())
                                                    .avatar(rel.getUser().getName())
                                            .build())
                                    .friendStatus(UserContactDTO.FriendStatus.RECEIVED_REQUEST)
                                    .createdAt(rel.getCreatedAt())
                                    .build();
                        })
                        .toList()
        );

        return paging;
    }

    @Override
    @Transactional
    public UserFriendDTO acceptFriendRequest(Long friendId) {
        var user = getAuthenticatedUser();

        if(relationshipRepo.findByIdAndType(user.getId(), friendId, Relationship.RelationshipType.FRIEND).isPresent()) {
            return null;
        }

        var friendRequest = relationshipRepo.findByIdAndType(friendId, user.getId(), Relationship.RelationshipType.FRIEND)
                .orElseThrow(() -> new ResourceNotFoundException("No friend request is found"));

        Relationship relationship = Relationship
                .builder()
                .id(new RelationshipId(user.getId(), friendId))
                .user(new User(user.getId()))
                .relatedUser(new User(friendId))
                .type(Relationship.RelationshipType.FRIEND)
                .createdAt(new Date())
                .build();


        friendRequest.setCreatedAt(new Date());

        relationship = relationshipRepo.save(relationship);
        relationshipRepo.save(friendRequest);

        eventPublisher.publishEvent(new FriendAcceptEvent(this, relationship));

        return UserFriendDTO
                .builder()
                .relatedUser(UserContactDTO.convert(relationship.getRelatedUser()))
                .friendStatus(UserContactDTO.FriendStatus.FRIEND)
                .build();
    }
}
