package com.campus.smartcampus.dto.response;

import com.campus.smartcampus.enums.UserRole;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private UserRole role;
    private boolean isActive;
    private boolean emailVerified;
    private Instant lastLogin;
    private Instant createdAt;
}
