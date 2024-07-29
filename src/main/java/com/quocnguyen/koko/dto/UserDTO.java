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
    private int status;
    private int gender;
    private Date birthdate;
    private Date createdAt;
}
