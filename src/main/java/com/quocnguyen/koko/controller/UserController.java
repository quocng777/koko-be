package com.quocnguyen.koko.controller;

import com.quocnguyen.koko.dto.AppResponse;
import com.quocnguyen.koko.dto.ErrorResponse;
import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.model.VerificationCode;
import com.quocnguyen.koko.service.UserService;
import com.quocnguyen.koko.service.VerificationCodeService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Quoc Nguyen on {8/1/2024}
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final VerificationCodeService verificationCodeService;

    @PostMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestBody Map<String, Object> payload) {
        String token = (String) payload.get("token");
        UserDTO user = null;

        if(token != null) {
            user = userService.verifyAccount(token);
        }

        if(user == null) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, "Invalid verified token"), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(AppResponse.success(user));
    }

    @GetMapping("/refresh-verify-code")
    public ResponseEntity<?>  refreshVerifyCode() {
        UserDTO user = userService.getAuthenticatedUser();
        VerificationCode verificationCode = verificationCodeService.createVerifyEmailCodeAndSendEmail(user);

        if(verificationCode != null) {
            return ResponseEntity.ok(AppResponse.success(null));
        }

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAuthentication() {
        UserDTO user = userService.getAuthenticatedUser();

        return new ResponseEntity<>(
                AppResponse.success(user), HttpStatus.OK
        );
    }

    @GetMapping("/friends")
    public ResponseEntity<?> getFriends(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                        @RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
                                        @RequestParam(value = "pageNum", defaultValue = "0") int pageNum) {
        return ResponseEntity.ok(
                AppResponse.success(userService.getFriends(keyword, pageNum, pageSize))
        );
    }

    @GetMapping("/friend-status")
    public ResponseEntity<?> checkFriend(@RequestParam(name = "friendId") Long id) {
        return ResponseEntity.ok(
                AppResponse.success(userService.checkFriendStatus(id))
        );
    }

    @GetMapping("/friends/request")
    public ResponseEntity<?> requestFriend(@RequestParam(name = "friendId") Long id) throws BadRequestException {
        var userContact = userService.requestFriend(id);
        if(userContact == null) {
            throw new BadRequestException("Bad request");
        }

        return ResponseEntity.ok(
                AppResponse.success(userContact)
        );
    }

    @GetMapping("/friends/received-requests")
    public ResponseEntity<?> getReceivedRequests(@RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                                 @RequestParam(name = "pageSize", defaultValue = "0") int pageSize) {
        return
                ResponseEntity.ok(
                        AppResponse.success(userService.getFriendRequests(pageNum, pageSize))
                );
    }

    @GetMapping("/friends/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestParam(name = "friendId") Long friendId) {
        return ResponseEntity.ok(
                AppResponse.success(userService.acceptFriendRequest(friendId))
        );
    }


}
