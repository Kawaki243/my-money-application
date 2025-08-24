package com.project.moneymanagerbackend.services;


import com.project.moneymanagerbackend.dto.AuthenticationDTO;
import com.project.moneymanagerbackend.dto.ProfileRequest;
import com.project.moneymanagerbackend.dto.ProfileResponse;
import com.project.moneymanagerbackend.entities.ProfileEntity;
import com.project.moneymanagerbackend.repositories.ProfileRepository;
import com.project.moneymanagerbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * Service layer for managing user profiles.
 * <p>
 * Handles profile registration, entity conversion, and response mapping.
 */
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // Used to authenticate user credentials (e.g., email and password)
    @Autowired
    private AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @Value("${app.application.url}")
    private String activationURL;

    /**
     * Registers a new user profile.
     * <p>
     * Converts the {@link ProfileRequest} to a {@link ProfileEntity},
     * generates a unique activation token, saves it to the database,
     * and returns a {@link ProfileResponse}.
     *
     * @param profileRequest the request containing profile details
     * @return the saved profile as a response DTO
     */
    public ProfileResponse registerProfile(ProfileRequest profileRequest) {
        ProfileEntity newProfile = toEntity(profileRequest);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile = this.profileRepository.save(newProfile);

        /** send Activation Link */
        String activationLink = this.activationURL+"/api/v1.0/activate?token=" + newProfile.getActivationToken();
        String subject = "Activate your My Money account";
        String body = "Click on the following link to activate your My Money account : "+activationLink;
        this.emailService.sendEmail(newProfile.getEmail(), subject, body);

        return toResponse(newProfile);
    }

    /**
     * Converts a {@link ProfileRequest} DTO into a {@link ProfileEntity}.
     *
     * @param profileRequest the request object containing profile data
     * @return a {@link ProfileEntity} built from the request data
     */
    public ProfileEntity toEntity(ProfileRequest profileRequest) {
        return ProfileEntity.builder()
                .id(profileRequest.getId())
                .fullName(profileRequest.getFullName())
                .email(profileRequest.getEmail())
                .password(this.passwordEncoder.encode(profileRequest.getPassword()))
                .profileImageUrl(profileRequest.getProfileImageUrl())
                .createdAt(profileRequest.getCreatedAt())
                .updatedAt(profileRequest.getUpdatedAt())
                .build();
    }

    /**
     * Converts a {@link ProfileEntity} into a {@link ProfileResponse} DTO.
     *
     * @param profileEntity the entity containing profile data
     * @return a {@link ProfileResponse} built from the entity data
     */
    public ProfileResponse toResponse(ProfileEntity profileEntity) {
        return ProfileResponse.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }

    /**
     * Activates a user profile based on the provided activation token.
     * <p>
     * Looks up the profile by its activation token. If found, the profile is marked
     * as active and saved back to the database. If no profile is found, activation fails.
     *
     * @param activationToken the unique token used to identify and activate the profile
     * @return {@code true} if the profile was successfully activated,
     *         {@code false} if no profile was found for the given token
     */
    public boolean activateProfile(String activationToken) {
        return this.profileRepository.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    this.profileRepository.save(profile);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Checks if a user account is active.
     *
     * @param email the email of the profile to check
     * @return {@code true} if the profile is active, {@code false} otherwise
     */
    public boolean isAccountActive(String email) {
        return this.profileRepository.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }

    /**
     * Retrieves the currently authenticated user's profile.
     *
     * @return the {@link ProfileEntity} of the logged-in user
     * @throws UsernameNotFoundException if the profile does not exist
     */
    public ProfileEntity getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return this.profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: " + email));
    }

    /**
     * Retrieves a public profile by email or, if no email is provided,
     * returns the profile of the currently authenticated user.
     *
     * @param email the email of the profile to fetch (nullable)
     * @return the {@link ProfileResponse} representing the profile
     * @throws UsernameNotFoundException if the profile does not exist
     */
    public ProfileResponse getPublicProfile(String email) {
        ProfileEntity currentProfile = null;
        if (email == null) {
            currentProfile = getCurrentProfile();
        } else {
            currentProfile = this.profileRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: " + email));
        }
        return toResponse(currentProfile);
    }

    /**
     * Authenticates a user with the provided credentials and generates a JWT token.
     *
     * @param authenticationDTO the login request containing email and password
     * @return a {@link Map} containing:
     *         <ul>
     *             <li>{@code token} – the generated JWT token (placeholder in this example)</li>
     *             <li>{@code user} – the authenticated user's public profile</li>
     *         </ul>
     * @throws UsernameNotFoundException if authentication fails due to invalid email or password
     */
    public Map<String, Object> authenticateGenerateToken(AuthenticationDTO authenticationDTO){
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword()));
            //Generate JWT token
            String token = this.jwtUtil.generateToken(authenticationDTO.getEmail());
            System.out.println("token: "+token);
            return Map.of(
                    "token", token,
                    "user", getPublicProfile(authenticationDTO.getEmail())
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password"+e.getMessage());
        }
    }
}

