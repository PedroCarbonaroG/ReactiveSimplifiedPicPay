package com.carbonaro.ReactiveSimplifiedPicPay.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @Schema(description = "Id unico da pessoa")
    private String id;

    @Schema(description = "Nome completo da pessoa")
    protected String name;

    @Schema(description = "Email da pessoa")
    private String email;

    @Schema(description = "Endereço completo da pessoa")
    private String address;

    @Schema(description = "Senha de acesso da pessoa")
    private String password;

}
