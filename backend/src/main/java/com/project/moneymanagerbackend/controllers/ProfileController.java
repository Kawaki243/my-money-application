package com.project.moneymanagerbackend.controllers;


import com.project.moneymanagerbackend.dto.AuthenticationDTO;
import com.project.moneymanagerbackend.dto.ProfileRequest;
import com.project.moneymanagerbackend.dto.ProfileResponse;
import com.project.moneymanagerbackend.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for managing user profiles.
 * <p>
 * Exposes endpoints for creating and managin userg profiles.
 */
@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Registers a new user profile.
     * <p>
     * Accepts a {@link ProfileRequest} in the request body, delegates the
     * registration logic to {@link ProfileService}, and returns a
     * {@link ProfileResponse} with HTTP status {@code 201 Created}.
     *
     * @param profileRequest the request body containing user profile details
     * @return a {@link ResponseEntity} with the created user profile and HTTP status 201
     */
    @PostMapping("/register")
    public ResponseEntity<ProfileResponse> createProfile(@RequestBody ProfileRequest profileRequest) {
        ProfileResponse registeredProfile = profileService.registerProfile(profileRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }

    /**
     * Activates a user profile using the provided activation token.
     * <p>
     * This endpoint verifies the activation token and updates the corresponding
     * profile to an active state if valid.
     *
     * @param token the unique token provided to activate the profile
     * @return {@link ResponseEntity} containing:
     *         <ul>
     *             <li>HTTP 200 (OK) with a success message if the profile was activated</li>
     *             <li>HTTP 404 (Not Found) with an error message if the token is invalid or already used</li>
     *         </ul>
     */
    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token) {
        boolean activated = profileService.activateProfile(token);
        if (activated) {
            return ResponseEntity.ok("Profile activated successfully");
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or already used");
        }
    }

    /**
     * Handles user login requests.
     * <p>
     * This endpoint validates whether the user's account is active, authenticates
     * their credentials, and generates a JWT token if authentication succeeds.
     * </p>
     *
     * @param authenticationDTO the login request payload containing email and password
     * @return {@link ResponseEntity} containing:
     *         <ul>
     *             <li>200 OK with a response map including the JWT token and user info if login is successful</li>
     *             <li>403 FORBIDDEN if the account is not yet activated</li>
     *             <li>400 BAD REQUEST if authentication fails (e.g., invalid credentials)</li>
     *         </ul>
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthenticationDTO authenticationDTO) {
        try {
            if (!profileService.isAccountActive(authenticationDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Account not active for this email : " + authenticationDTO.getEmail() + ". Please activate your account first !"));
            }
            Map<String, Object> response = profileService.authenticateGenerateToken(authenticationDTO);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(){
        ProfileResponse profileResponse = this.profileService.getPublicProfile(null);
        return ResponseEntity.ok(profileResponse);
    }
}

