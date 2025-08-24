package com.project.moneymanagerbackend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/** This is the "Profile" Object Response Class sent from the Server to the Client-side */
public class ProfileResponse {

    /** Primary key of the profile entity. */
    private Long id;

    /** User's full name. */
    private String fullName;

    /** User's email address, typically used for login and communication. */
    private String email;

    /** URL pointing to the user's profile image. */
    private String profileImageUrl;

    /** Timestamp of when the profile was created. */
    private LocalDateTime createdAt;

    /** Timestamp of the last update to the profile. */
    private LocalDateTime updatedAt;
}
