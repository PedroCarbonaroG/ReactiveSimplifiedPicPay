package com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NaturalPersonResponse extends PersonResponse {

    @Schema(description = "Unique demonstration of the NaturalPerson CPF.")
    private String cpf;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Schema(description = "NaturalPerson birthdate.")
    private LocalDate birthDate;

}