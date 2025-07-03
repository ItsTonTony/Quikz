package com.echofyteam.backend.exception.response;

import lombok.Builder;

@Builder
public record InvalidParameterResponse(
        String parameter,
        String message
) {
}