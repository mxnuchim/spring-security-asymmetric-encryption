package com.mxnuchim.auth.services.impl;

import com.mxnuchim.auth.domain.entities.Role;
import com.mxnuchim.auth.domain.entities.User;
import com.mxnuchim.auth.dto.request.LoginRequest;
import com.mxnuchim.auth.dto.request.RefreshRequest;
import com.mxnuchim.auth.dto.request.RegistrationRequest;
import com.mxnuchim.auth.dto.response.AuthenticationResponse;
import com.mxnuchim.auth.exceptions.BusinessException;
import com.mxnuchim.auth.exceptions.ErrorCode;
import com.mxnuchim.auth.mappers.UserMapper;
import com.mxnuchim.auth.repositories.RoleRepository;
import com.mxnuchim.auth.repositories.UserRepository;
import com.mxnuchim.auth.security.JwtService;
import com.mxnuchim.auth.services.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private  final UserMapper userMapper;

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        final Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        final User user = (User) auth.getPrincipal();
        final String token = this.jwtService.generateAccessToken(user.getUsername());
        final String refreshToken = this.jwtService.generateRefreshToken(user.getUsername());
        final String tokenType = "Bearer";

        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .tokenType(tokenType)
                .build();

    }

    @Override
    @Transactional
    public void register(RegistrationRequest request) {
        checkUserEmail(request.getEmail());
        checkPhoneNumber(request.getPhoneNumber());
        checkPasswords(request.getPassword(), request.getConfirmPassword());

        final Role userRole = this.roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("User role does not exist"));

        final List<Role> roles = new ArrayList<>();
        roles.add(userRole);

        final User user = this.userMapper.toUser(request);
        user.setRoles(roles);
        log.debug("Saving user {}", user);
        this.userRepository.save(user);

        final List<User> users = new ArrayList<>();
        users.add(user);
        userRole.setUsers(users);

        this.roleRepository.save(userRole);
    }



    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) {
        final String newAccessToken = this.jwtService.refreshAccessToken(request.getRefreshToken());
        final String tokenType = "Bearer";

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.getRefreshToken())
                .tokenType(tokenType)
                .build();
    }

    private void checkUserEmail(final String email) {
        final boolean emailExists = this.userRepository.existsByEmailIgnoreCase(email);
        if(emailExists){
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

    }


    private void checkPhoneNumber(final String phoneNumber) {
        final boolean phoneExists = this.userRepository.existsByPhoneNumber(phoneNumber);
        if(phoneExists){
            throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS);
        }
    }

    private void checkPasswords(final String password, final String confirmPassword) {
        if(password == null || confirmPassword == null){
            throw new BusinessException(ErrorCode.MISSING_PASSWORDS);
        }

        if(!password.equals(confirmPassword)){
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }
    }
}
