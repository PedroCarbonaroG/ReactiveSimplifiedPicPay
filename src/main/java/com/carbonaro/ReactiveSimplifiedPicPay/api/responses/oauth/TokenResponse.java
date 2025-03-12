package com.carbonaro.ReactiveSimplifiedPicPay.api.responses.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    private Long expirationIn;
    private String expirationInDescription;
    private String tokenType;
    private String accessToken;

}
