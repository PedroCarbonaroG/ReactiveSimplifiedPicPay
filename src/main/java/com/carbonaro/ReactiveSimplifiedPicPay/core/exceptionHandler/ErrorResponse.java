package com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @Schema(description = "Horário exato em que a excessão ocorreu")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestemp;

    @Schema(description = "Caminho onde ocorreu a excessão")
    private String path;

    @Schema(description = "Status do erro")
    private Integer status;

    @Schema(description = "Qual foi o erro em propriamente dito")
    private String error;

    @Schema(description = "Mensagem sobre o respectivo erro")
    private String errorMessage;

}
