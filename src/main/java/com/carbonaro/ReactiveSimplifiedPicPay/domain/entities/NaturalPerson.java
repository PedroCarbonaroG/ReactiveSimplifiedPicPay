package com.carbonaro.ReactiveSimplifiedPicPay.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.Description;
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

    @Indexed(unique = true)
    @Description("Unique NaturalPerson CPF.")
    private String cpf;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Description("NaturalPerson birthdate.")
    private LocalDate birthDate;

    @Description("NaturalPerson unique bank balance.")
    private BigDecimal balance;

}
