package com.reliaquest.api.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private String status;
    private String message;
    private Instant timestamp;

    public static ApiErrorResponse of(String status, String message) {
        return new ApiErrorResponse(status, message, Instant.now());
    }
}
