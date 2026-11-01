package com.luizmrd.crm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.rate-limit")
public class RateLimitProperties {

    private boolean enabled = true;
    private long loginCapacidade = 10;
    private long loginJanelaSegundos = 60;
    private long apiCapacidade = 100;
    private long apiJanelaSegundos = 60;
}
