package com.bossj.govyreel.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    @Builder.Default
    private boolean success = true;

    @Builder.Default
    private String message = "Request processed successfully";

    private T data;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}