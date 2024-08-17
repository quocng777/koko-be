package com.quocnguyen.koko.dto;

import com.quocnguyen.koko.model.Participant;
import com.quocnguyen.koko.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author Quoc Nguyen on {2024-08-14}
 */

@Data
@Builder
public class ParticipantDTO {
    private Long userId;
    private String userAvatar;
    private String username;
    private String name;
    private Date createdAt;

    public static ParticipantDTO convert(Participant participant) {
        User user = participant.getUser();

        ParticipantDTO dto = ParticipantDTO
                .builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .createdAt(participant.getCreateAt())
                .build();

        return dto;
    }
}
