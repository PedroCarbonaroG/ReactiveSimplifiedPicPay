package com.carbonaro.ReactiveSimplifiedPicPay.api.helper;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;

import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.UserExchangeMutator;

@UtilityClass
public class BaseHelper {

    private static final String[] SECURITY_ROLES = new String[]{"USER", "ADMIN"};

    public static UserExchangeMutator disableSecurity() {
        return mockUser().roles(SECURITY_ROLES);
    }

    public static <T> Optional<T> getOptionalField(T field) {
        return Optional.ofNullable(field);
    }

}
