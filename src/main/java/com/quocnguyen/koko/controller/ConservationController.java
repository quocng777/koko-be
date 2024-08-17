package com.quocnguyen.koko.controller;

import com.quocnguyen.koko.dto.AppResponse;
import com.quocnguyen.koko.dto.ConservationDTO;
import com.quocnguyen.koko.dto.ConservationRequestParams;
import com.quocnguyen.koko.service.ConservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

/**
 * @author Quoc Nguyen on {2024-08-13}
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conservations")
public class ConservationController {
    private final ConservationService conservationService;

    @PostMapping("")
    public ResponseEntity<?> createConservation(@RequestBody  ConservationRequestParams params) {
        ConservationDTO conservation = conservationService.create(params);

        return new ResponseEntity<>(AppResponse.created(conservation), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getConservations() {
        return ResponseEntity.ok(
                AppResponse.success(conservationService.getConservations()));
    }
}
