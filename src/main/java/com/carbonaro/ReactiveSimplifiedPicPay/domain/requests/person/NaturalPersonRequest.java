package com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NaturalPersonRequest extends PersonRequest {

    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Description("NaturalPerson birthdate.")
    private LocalDate birthDate;

}
