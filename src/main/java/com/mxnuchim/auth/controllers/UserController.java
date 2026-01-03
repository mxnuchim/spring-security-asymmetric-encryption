package com.mxnuchim.auth.controllers;

import com.mxnuchim.auth.dto.ChangePasswordDto;
import com.mxnuchim.auth.dto.ProfileUpdateDto;
import com.mxnuchim.auth.domain.entities.User;
import com.mxnuchim.auth.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User Management Endpoints")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get user profile",
            description = "Retrieve the authenticated user's profile information including first name, last name, email, phone number, and profile picture."
    )
    public User getProfile(final Authentication principal) {
        // Cast principal to existing User entity
        return (User) principal.getPrincipal();
    }

    @PatchMapping("/profile")
    @Operation(
            summary = "Update user profile",
            description = "Update the authenticated user's profile information such as first name, last name, or profile picture."
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfileInfo(
            @RequestBody
            @Valid
            final ProfileUpdateDto dto,
            final Authentication principal
            ){
        this.userService.updateProfileInfo(getUserId(principal), dto);

    }

    @PostMapping("/profile/password")
    @Operation(
            summary = "Change password",
            description = "Change the authenticated user's password. Requires oldPassword and newPassword fields in the body."
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @RequestBody
            @Valid
            final ChangePasswordDto dto,
            final Authentication principal
    ){
         this.userService.changePassword(getUserId(principal), dto);
    }

    @PatchMapping("/profile/deactivate")
    @Operation(
            summary = "Deactivate account",
            description = "Deactivate the authenticated user's account. The user will not be able to login until the account is reactivated."
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateAccount(final Authentication principal){
        this.userService.deactivateAccount(getUserId(principal));
    }

    @PatchMapping("/profile/reactivate")
    @Operation(
            summary = "Reactivate account",
            description = "Reactivate the authenticated user's previously deactivated account. User can login again after reactivation."
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reactivateAccount(final Authentication principal){
        this.userService.reactivateAccount(getUserId(principal));
    }

    @DeleteMapping("/profile")
    @Operation(
            summary = "Delete account",
            description = "Permanently delete the authenticated user's account. This action is irreversible."
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(final Authentication principal){
        this.userService.deleteAccount(getUserId(principal));
    }

    private UUID getUserId(final Authentication principal) {
       return ((User) principal.getPrincipal()).getId();
    };

}
