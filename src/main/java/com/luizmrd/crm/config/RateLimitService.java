package com.luizmrd.crm.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private final RateLimitProperties properties;
    private final Cache<String, Bucket> loginBuckets;
    private final Cache<String, Bucket> apiBuckets;

    public RateLimitService(RateLimitProperties properties) {
        this.properties = properties;
        this.loginBuckets = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofHours(1))
                .maximumSize(10_000)
                .build();
        this.apiBuckets = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofHours(1))
                .maximumSize(50_000)
                .build();
    }

    public ConsumptionProbe consumirLogin(String identificador) {
        Bucket bucket = loginBuckets.get(identificador, k -> criarBucket(
                properties.getLoginCapacidade(),
                properties.getLoginJanelaSegundos()
        ));
        return bucket.tryConsumeAndReturnRemaining(1);
    }

    public ConsumptionProbe consumirApi(String identificador) {
        Bucket bucket = apiBuckets.get(identificador, k -> criarBucket(
                properties.getApiCapacidade(),
                properties.getApiJanelaSegundos()
        ));
        return bucket.tryConsumeAndReturnRemaining(1);
    }

    private Bucket criarBucket(long capacidade, long janelaSegundos) {
        Bandwidth limite = Bandwidth.builder()
                .capacity(capacidade)
                .refillIntervally(capacidade, Duration.ofSeconds(janelaSegundos))
                .build();
        return Bucket.builder()
                .addLimit(limite)
                .build();
    }
}
