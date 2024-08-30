package com.quocnguyen.koko.dto;

import com.quocnguyen.koko.model.BroadCastInfo;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Quoc Nguyen on {2024-08-30}
 */

@Data
@Builder
public class MessageBroadcastDTO {
    private BroadCastInfo.BroadCastType broadcastType;
    private Set<UserContactDTO> targetUsers;

    public static MessageBroadcastDTO convert(BroadCastInfo broadCastInfo) {
        if(broadCastInfo == null)
            return null;

        return MessageBroadcastDTO
                .builder()
                .broadcastType(broadCastInfo.getBroadCastType())
                .targetUsers(
                        broadCastInfo.getTargetUsers()
                                .stream()
                                .map(UserContactDTO::convert)
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
