package com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEmptyResponse {

    @Schema(description = "Horário exato em que o aviso ocorreu")
    @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Caminho onde ocorreu o aviso")
    private String path;

    @Schema(description = "Status do aviso")
    private Integer status;

    @Schema(description = "Numero HTTP do erro/aviso")
    private String error;

    @Schema(description = "Mesagem de auxilio do aviso")
    private String warningMessage;
}
