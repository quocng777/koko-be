package com.quocnguyen.koko.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private boolean enabled;
    private int gender;
    private Date birthdate;
    private Date createdAt;
    private boolean verified;
}
