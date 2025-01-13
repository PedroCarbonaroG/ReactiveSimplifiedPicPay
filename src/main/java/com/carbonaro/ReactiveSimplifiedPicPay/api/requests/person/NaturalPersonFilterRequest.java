package com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.PagedRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NaturalPersonFilterRequest extends PagedRequest {

    @Schema(description = "Natural birthDate, ex: (dd/mm/yyyy)")
    private LocalDate birthDate;

    @Schema(description = "Natural name, ex: pedro")
    private String name;

    @Schema(description = "Natural email, ex: pedro@email.com")
    private String email;

    @Schema(description = "Natural address, ex: nowhere street, 152")
    private String address;

}
