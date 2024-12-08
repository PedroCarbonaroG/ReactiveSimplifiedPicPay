package com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LegalPersonRequest extends PersonRequest {

    @Schema(description = "Number of employees in the company")
    private int employeesNumber;

}
