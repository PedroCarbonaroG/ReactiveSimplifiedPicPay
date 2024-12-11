package com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.PagedRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NaturalPersonFilterRequest extends PagedRequest {

    private String cpf;
    private String name;
    private String email;
    private String address;
    private LocalDate initialBirthDate;
    private LocalDate finalBirthDate;

}
