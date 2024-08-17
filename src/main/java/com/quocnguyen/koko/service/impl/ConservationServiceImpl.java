package com.quocnguyen.koko.service.impl;

import com.quocnguyen.koko.dto.ConservationDTO;
import com.quocnguyen.koko.dto.ConservationRequestParams;
import com.quocnguyen.koko.dto.UserDTO;
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
                .type(params.getConservationType())
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
                        if(params.getConservationType() == Conservation.ConservationType.GROUP
                                && elm.getId().equals(user.getId())) {
                            participant.setRole(Participant.Role.ADMIN);
                        }

                        participant = participantRepository.save(participant);
                        return participant;
                    }).collect(Collectors.toSet());

            conservation.setParticipants(participants);

            return ConservationDTO.convert(savedConservation);

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
}
