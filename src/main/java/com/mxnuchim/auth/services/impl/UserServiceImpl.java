package com.mxnuchim.auth.services.impl;

import com.mxnuchim.auth.dto.ChangePasswordDto;
import com.mxnuchim.auth.dto.ProfileUpdateDto;
import com.mxnuchim.auth.domain.entities.User;
import com.mxnuchim.auth.exceptions.BusinessException;
import com.mxnuchim.auth.exceptions.ErrorCode;
import com.mxnuchim.auth.mappers.UserMapper;
import com.mxnuchim.auth.repositories.UserRepository;
import com.mxnuchim.auth.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return this.userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with email -->> " + email));
    }

    @Override
    public void updateProfileInfo(UUID userId, ProfileUpdateDto dto) {
        final User savedUser = this.userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        this.userMapper.mergeUserInfo(savedUser, dto);
        this.userRepository.save(savedUser);
    };

    @Override
    public void changePassword(UUID userId, ChangePasswordDto dto) {
        if(!dto.getNewPassword().equals(dto.getConfirmNewPassword())){
            throw new BusinessException(ErrorCode.CHANGE_PASSWORDS_MISMATCH);
        }

        final User savedUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(!this.passwordEncoder.matches(dto.getCurrentPassword(), savedUser.getPassword())){
            throw new BusinessException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        final String encodedPassword = this.passwordEncoder.encode(dto.getCurrentPassword());

        savedUser.setPassword(encodedPassword);
        this.userRepository.save(savedUser);

    }

    @Override
    public void deactivateAccount(UUID userId) {

        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(!user.isEnabled()){
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DEACTIVATED);
        }

        user.setEnabled(false);
        this.userRepository.save(user);
    }

    @Override
    public void reactivateAccount(UUID userId) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(user.isEnabled()){
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_ACTIVATED);
        }

        user.setEnabled(true);
        this.userRepository.save(user);
    }

    @Override
    public void deleteAccount(UUID userId) {
        // this needs the rest of the entities
        // Essentially, the logic should just be to schedule the profile/user for deletion as well as other items related to the user
        // IDEA -->> A scheduled job can handle this at intervals, pick this up and delete everything relating to the user across all tables
    }
}
