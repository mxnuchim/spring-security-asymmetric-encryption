package com.mxnuchim.auth.services;

import com.mxnuchim.auth.dto.request.LoginRequest;
import com.mxnuchim.auth.dto.request.RefreshRequest;
import com.mxnuchim.auth.dto.request.RegistrationRequest;
import com.mxnuchim.auth.dto.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse login(LoginRequest request);

    void register(RegistrationRequest request);

    AuthenticationResponse refreshToken(RefreshRequest request);
}
