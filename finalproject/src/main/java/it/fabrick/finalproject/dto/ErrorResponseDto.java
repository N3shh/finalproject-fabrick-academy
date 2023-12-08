package it.fabrick.finalproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.fabrick.finalproject.enumeration.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponseDto {

    @Schema(description = "A predefined, documented error code, related to some predefined, documented scenario")
    private ErrorCode errorCode;
    @Schema(description = "A brief description of the error")
    private String errorMessage;
}
