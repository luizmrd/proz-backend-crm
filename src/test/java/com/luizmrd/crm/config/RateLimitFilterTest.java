package com.luizmrd.crm.config;

import com.luizmrd.crm.database.model.UsuarioEntity;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateLimitFilterTest {

    private RateLimitProperties properties;
    private RateLimitService rateLimitService;
    private RateLimitFilter rateLimitFilter;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        properties = new RateLimitProperties();
        properties.setEnabled(true);
        properties.setLoginCapacidade(2);
        properties.setLoginJanelaSegundos(60);
        properties.setApiCapacidade(3);
        properties.setApiJanelaSegundos(60);

        rateLimitService = new RateLimitService(properties);
        rateLimitFilter = new RateLimitFilter(rateLimitService, properties, new ObjectMapper());
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve permitir requisições de login dentro do limite e bloquear ao exceder a capacidade por IP")
    void deveBloquearLoginAoExcederLimitePorIp() throws Exception {
        for (int i = 0; i < 2; i++) {
            MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/auth/login");
            request.setRemoteAddr("192.168.1.10");
            MockHttpServletResponse response = new MockHttpServletResponse();

            rateLimitFilter.doFilter(request, response, filterChain);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertEquals(String.valueOf(1 - i), response.getHeader(RateLimitFilter.HEADER_RATE_LIMIT_REMAINING));
        }

        MockHttpServletRequest bloqueadaReq = new MockHttpServletRequest("POST", "/api/v1/auth/login");
        bloqueadaReq.setRemoteAddr("192.168.1.10");
        MockHttpServletResponse bloqueadaResp = new MockHttpServletResponse();

        rateLimitFilter.doFilter(bloqueadaReq, bloqueadaResp, filterChain);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), bloqueadaResp.getStatus());
        assertNotNull(bloqueadaResp.getHeader(RateLimitFilter.HEADER_RETRY_AFTER_SECONDS));
        assertTrue(bloqueadaResp.getContentAsString().contains("LIMITE_REQUISICOES_EXCEDIDO"));
        verify(filterChain, times(2)).doFilter(any(), any());
    }

    @Test
    @DisplayName("Deve isolar os limites de API por usuário autenticado")
    void deveIsolarLimiteApiPorUsuarioAutenticado() throws Exception {
        UsuarioEntity usuario1 = new UsuarioEntity();
        usuario1.setId(1L);
        usuario1.setEmail("user1@crm.com");

        UsuarioEntity usuario2 = new UsuarioEntity();
        usuario2.setId(2L);
        usuario2.setEmail("user2@crm.com");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(usuario1, null, List.of())
        );

        for (int i = 0; i < 3; i++) {
            MockHttpServletRequest req = new MockHttpServletRequest("GET", "/api/v1/alunos");
            MockHttpServletResponse resp = new MockHttpServletResponse();
            rateLimitFilter.doFilter(req, resp, filterChain);
            assertEquals(HttpStatus.OK.value(), resp.getStatus());
        }

        MockHttpServletRequest reqBloqueadaUser1 = new MockHttpServletRequest("GET", "/api/v1/alunos");
        MockHttpServletResponse respBloqueadaUser1 = new MockHttpServletResponse();
        rateLimitFilter.doFilter(reqBloqueadaUser1, respBloqueadaUser1, filterChain);
        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), respBloqueadaUser1.getStatus());

        // Usuário 2 deve ter seu próprio bucket intacto
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(usuario2, null, List.of())
        );
        MockHttpServletRequest reqUser2 = new MockHttpServletRequest("GET", "/api/v1/alunos");
        MockHttpServletResponse respUser2 = new MockHttpServletResponse();
        rateLimitFilter.doFilter(reqUser2, respUser2, filterChain);
        assertEquals(HttpStatus.OK.value(), respUser2.getStatus());
        assertEquals("2", respUser2.getHeader(RateLimitFilter.HEADER_RATE_LIMIT_REMAINING));
    }

    @Test
    @DisplayName("Deve ignorar rotas do Swagger e requisições OPTIONS")
    void deveIgnorarRotasIsentas() throws Exception {
        MockHttpServletRequest swaggerReq = new MockHttpServletRequest("GET", "/swagger-ui/index.html");
        MockHttpServletResponse swaggerResp = new MockHttpServletResponse();

        rateLimitFilter.doFilter(swaggerReq, swaggerResp, filterChain);

        assertNull(swaggerResp.getHeader(RateLimitFilter.HEADER_RATE_LIMIT_REMAINING));
        verify(filterChain, times(1)).doFilter(swaggerReq, swaggerResp);
    }
}
