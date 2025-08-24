package com.project.moneymanagerbackend.services;

import com.project.moneymanagerbackend.dto.CategoryDTO;
import com.project.moneymanagerbackend.entities.ProfileEntity;
import com.project.moneymanagerbackend.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Custom implementation of {@link UserDetailsService} for loading user details
 * during authentication.
 * <p>
 * Retrieves a {@link ProfileEntity} from the database by email and maps it to
 * Spring Security's {@link UserDetails}.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    /**
     * Loads a user by their email address.
     * <p>
     * This method is invoked automatically by Spring Security during the authentication process.
     *
     * @param email the email address of the user to load
     * @return a {@link UserDetails} instance containing user credentials
     * @throws UsernameNotFoundException if no user is found for the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ProfileEntity existingEntity = profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with the email: " + email));

        return User.builder()
                // Recommend using email as the username, not fullName
                .username(existingEntity.getEmail())
                .password(existingEntity.getPassword())
                // No roles/authorities assigned yet; can be extended later
                .authorities(Collections.emptyList())
                .build();
    }


}

