package com.whosbigboy.tutandtam.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public final class AuthDtos {
    private AuthDtos() {
    }

    public record ContractAuthRequest(
            @NotBlank @Size(max = 100) String contractNumber
    ) {
    }

    public record ContractAuthResponse(
            boolean found,
            List<TouristIdentity> members
    ) {
    }

    public record TouristIdentity(
            String touristId,
            String fio
    ) {
    }

    public record LoginRequest(
            @NotBlank @Email @Size(max = 100) String email,
            @NotBlank @Size(max = 100) String password
    ) {
    }

    public record LoginResponse(
            boolean authenticated,
            String userId,
            String fio
    ) {
    }

    public record SupportRequest(
            @NotBlank @Size(max = 1000) String message
    ) {
    }

    public record FeedbackRequest(
            @NotBlank String tourId,
            @NotNull Integer rating,
            @NotBlank @Size(max = 255) String text
    ) {
    }
}
