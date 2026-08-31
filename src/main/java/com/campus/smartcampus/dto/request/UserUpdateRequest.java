package com.campus.smartcampus.dto.request;

import com.campus.smartcampus.enums.UserRole;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private UserRole role;
    private Boolean isActive;
    private Boolean emailVerified;
}
