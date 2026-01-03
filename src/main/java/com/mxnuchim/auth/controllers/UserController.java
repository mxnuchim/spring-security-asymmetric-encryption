package com.mxnuchim.auth.controllers;

import com.mxnuchim.auth.dto.ChangePasswordDto;
import com.mxnuchim.auth.dto.ProfileUpdateDto;
import com.mxnuchim.auth.domain.entities.User;
import com.mxnuchim.auth.services.UserService;
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

    @PatchMapping("/profile")
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateAccount(final Authentication principal){
        this.userService.deactivateAccount(getUserId(principal));
    }

    @PatchMapping("/profile/reactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reactivateAccount(final Authentication principal){
        this.userService.reactivateAccount(getUserId(principal));
    }

    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(final Authentication principal){
        this.userService.deleteAccount(getUserId(principal));
    }

    private UUID getUserId(final Authentication principal) {
       return ((User) principal.getPrincipal()).getId();
    };

}
