package com.luizmrd.crm.config;

import com.luizmrd.crm.database.model.UsuarioEntity;
import com.luizmrd.crm.exception.ErrorResponse;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    public static final String HEADER_RATE_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
    public static final String HEADER_RETRY_AFTER_SECONDS = "X-Rate-Limit-Retry-After-Seconds";

    private static final String ROTA_LOGIN = "/api/v1/auth/login";
    private static final String PREFIXO_API = "/api/";

    private final RateLimitService rateLimitService;
    private final RateLimitProperties properties;
    private final ObjectMapper objectMapper;

    public RateLimitFilter(RateLimitService rateLimitService,
                           RateLimitProperties properties,
                           ObjectMapper objectMapper) {
        this.rateLimitService = rateLimitService;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!properties.isEnabled()) {
            return true;
        }
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        String uri = request.getRequestURI();
        return uri == null || !uri.startsWith(PREFIXO_API);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ConsumptionProbe probe;
        if (ehRequisicaoLogin(request)) {
            String ip = extrairIpCliente(request);
            probe = rateLimitService.consumirLogin("ip:" + ip);
        } else {
            String identificador = resolverIdentificadorCliente(request);
            probe = rateLimitService.consumirApi(identificador);
        }

        if (probe.isConsumed()) {
            response.setHeader(HEADER_RATE_LIMIT_REMAINING, String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
            return;
        }

        long segundosParaRecarga = Math.max(1L, TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()));
        response.setHeader(HEADER_RETRY_AFTER_SECONDS, String.valueOf(segundosParaRecarga));
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorResponse erro = ErrorResponse.of(
                "LIMITE_REQUISICOES_EXCEDIDO",
                "Muitas requisições realizadas. Tente novamente em alguns segundos.",
                Map.of("retryAfterSeconds", segundosParaRecarga)
        );

        objectMapper.writeValue(response.getWriter(), erro);
    }

    private boolean ehRequisicaoLogin(HttpServletRequest request) {
        return HttpMethod.POST.matches(request.getMethod())
                && ROTA_LOGIN.equals(request.getRequestURI());
    }

    private String resolverIdentificadorCliente(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UsuarioEntity usuario && usuario.getId() != null) {
                return "user:" + usuario.getId();
            }
            if (principal instanceof UsuarioEntity usuario && usuario.getEmail() != null) {
                return "user:" + usuario.getEmail();
            }
            if (auth.getName() != null && !"anonymousUser".equals(auth.getName())) {
                return "user:" + auth.getName();
            }
        }
        return "ip:" + extrairIpCliente(request);
    }

    private String extrairIpCliente(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String remoteAddr = request.getRemoteAddr();
        return (remoteAddr != null && !remoteAddr.isBlank()) ? remoteAddr : "unknown";
    }
}
