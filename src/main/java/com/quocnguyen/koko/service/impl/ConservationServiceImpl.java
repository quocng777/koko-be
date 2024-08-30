package com.quocnguyen.koko.service.impl;

import com.quocnguyen.koko.dto.AppPaging;
import com.quocnguyen.koko.dto.ConservationDTO;
import com.quocnguyen.koko.dto.ConservationRequestParams;
import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.event.GroupCreateEvent;
import com.quocnguyen.koko.exception.ResourceNotFoundException;
import com.quocnguyen.koko.model.Conservation;
import com.quocnguyen.koko.model.Participant;
import com.quocnguyen.koko.model.User;
import com.quocnguyen.koko.repository.ConservationRepository;
import com.quocnguyen.koko.repository.ParticipantRepository;
import com.quocnguyen.koko.repository.UserRepository;
import com.quocnguyen.koko.service.ConservationService;
import com.quocnguyen.koko.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Quoc Nguyen on {2024-08-13}
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ConservationServiceImpl implements ConservationService {
    private final ConservationRepository conservationRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ConservationDTO create(ConservationRequestParams params) {

        log.info("Request" + params);

        UserDTO user = userService.getAuthenticatedUser();

        // firstly check participants
        params
                .getParticipants()
                .stream()
                .filter((elm)-> elm.equals(user.getId()))
                .findFirst()
                .orElseThrow();

        Conservation conservation = Conservation
                .builder()
                .name(params.getName())
                .creator(new User(user.getId()))
                .createdAt(new Date())
                .type(params.getType())
                .build();

        try {
            var users = userRepository.findUsersById(params.getParticipants());

            Conservation savedConservation = conservationRepository.save(conservation);
            // covert users to participant entities
            Set<Participant> participants = users
                    .stream()
                    .map(elm -> {
                        Participant participant = Participant
                                .builder()
                                .createAt(new Date())
                                .user(elm)
                                .conservation(conservation)
                                .build();
                        if(params.getType() == Conservation.ConservationType.GROUP
                                && elm.getId().equals(user.getId())) {
                            participant.setRole(Participant.Role.ADMIN);
                        }

                        participant = participantRepository.save(participant);
                        return participant;
                    }).collect(Collectors.toSet());

            conservation.setParticipants(participants);

            ConservationDTO res = ConservationDTO.convert(savedConservation);
            if(res.getType() == Conservation.ConservationType.GROUP) {
                eventPublisher.publishEvent(new GroupCreateEvent(this, res));
            }
            return res;

        } catch (Exception exc) {
            log.error(exc.getMessage());
            throw new ResourceNotFoundException("Have an error when inserting participant into group");
        }
    }

    @Override
    public Collection<ConservationDTO> getConservations() {
        UserDTO user = userService.getAuthenticatedUser();

        var conservations = conservationRepository.findAllByUserId(user.getId());

        return conservations.stream()
                .map(ConservationDTO::convert)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ConservationDTO get(Long id) {
        // check is user requesting is have the permission to access the conservation
        UserDTO user = userService.getAuthenticatedUser();

        Conservation conservation = conservationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Conservation with id %d is not found", id)));

        return ConservationDTO.convert(conservation);
    }

    @Override
    public AppPaging<ConservationDTO> getConservation(int pageNum, int pageSize, String keyword) {
        var user = userService.getAuthenticatedUser();

        Pageable pageable = PageRequest.of(pageNum, pageSize);

        var page = conservationRepository.findConservations(user.getId(), keyword, pageable);

        AppPaging<ConservationDTO> paging = AppPaging.convertExcludeContent(page);
        paging.setList(
                page.getContent()
                        .stream()
                        .map(ConservationDTO::convert)
                        .toList()
        );

        return paging;
    }
}
