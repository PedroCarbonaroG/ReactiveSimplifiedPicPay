package com.carbonaro.ReactiveSimplifiedPicPay.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "NaturalPerson")
public class NaturalPerson extends Person {

    @Schema(description = "CPF unico da pessoa física")
    @Indexed(unique = true)
    private String cpf;

    @Schema(description = "Data de nascimento da pessoa física")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    @Schema(description = "Saldo bancário da pessoa física")
    private BigDecimal balance;

}
