package com.quocnguyen.koko.dto;

import com.quocnguyen.koko.model.Conservation;
import com.quocnguyen.koko.model.Participant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Quoc Nguyen on {2024-08-13}
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConservationDTO {
    private Long id;
    private String name;
    private Set<ParticipantDTO> participants;
    private Date createdAt;
    private Conservation.ConservationType type;

    public static ConservationDTO convert(Conservation conservation) {
        Set<ParticipantDTO> participants = conservation
                .getParticipants()
                .stream()
                .map(elm -> ParticipantDTO.convert(elm))
                .collect(Collectors.toSet());

        ConservationDTO dto = builder()
                .id(conservation.getId())
                .name(conservation.getName())
                .participants(participants)
                .type(conservation.getType())
                .createdAt(conservation.getCreatedAt())
                .build();

        return dto;
    }

}
