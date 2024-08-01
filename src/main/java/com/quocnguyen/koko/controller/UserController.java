package com.quocnguyen.koko.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Quoc Nguyen on {8/1/2024}
 */

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping()
    public String testGetMethod() {
        return "hello";
    }


}
