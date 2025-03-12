package com.carbonaro.ReactiveSimplifiedPicPay.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "NaturalPerson")
@EqualsAndHashCode(callSuper = true)
public class NaturalPerson extends Person {

    @Indexed(unique = true)
    @Description("Unique NaturalPerson CPF.")
    private String cpf;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Description("NaturalPerson birthdate.")
    private LocalDate birthDate;

}
