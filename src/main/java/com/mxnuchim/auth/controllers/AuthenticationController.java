package com.mxnuchim.auth.controllers;

import com.mxnuchim.auth.dto.request.LoginRequest;
import com.mxnuchim.auth.dto.request.RefreshRequest;
import com.mxnuchim.auth.dto.request.RegistrationRequest;
import com.mxnuchim.auth.dto.response.AuthenticationResponse;
import com.mxnuchim.auth.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Asymmetric Encryption Authentication API")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (
            @Valid
            @RequestBody
            final LoginRequest request
            ){
        return ResponseEntity.ok(this.authenticationService.login(request));
    };

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid
            @RequestBody
            final RegistrationRequest request
    ){
        this.authenticationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    };

    @PostMapping("/refsreh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestBody
            final RefreshRequest request
            ){
            return ResponseEntity.ok(this.authenticationService.refreshToken(request));
    }
}
