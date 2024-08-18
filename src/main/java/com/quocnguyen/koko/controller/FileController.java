package com.quocnguyen.koko.controller;

import com.quocnguyen.koko.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Quoc Nguyen on {2024-08-18}
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;

    @GetMapping("/preSignedUrl")
    public ResponseEntity<?> getPreSignedUrl() {

        return ResponseEntity.ok(fileService.generatePreSignedUrl());
    };

}
