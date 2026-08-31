package com.campus.smartcampus.security;

import com.campus.smartcampus.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS_PER_MINUTE = 120;
    private static final long WINDOW_MS = 60000;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String clientIp = getClientIp(request);
        long now = System.currentTimeMillis();

        RequestCounter counter = requestCounts.compute(clientIp, (key, current) -> {
            if (current == null || (now - current.timestamp) > WINDOW_MS) {
                return new RequestCounter(new AtomicInteger(1), now);
            }
            current.count.incrementAndGet();
            return current;
        });

        if (counter.count.get() > MAX_REQUESTS_PER_MINUTE) {
            log.warn("Rate limit exceeded for IP: {}", clientIp);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(429);

            ErrorResponse.ErrorDetail errorDetail = ErrorResponse.ErrorDetail.builder()
                    .code("RATE_LIMIT_EXCEEDED")
                    .message("Too many requests. Please try again later.")
                    .details(List.of("Maximum allowed requests is " + MAX_REQUESTS_PER_MINUTE + " per minute"))
                    .build();

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .success(false)
                    .error(errorDetail)
                    .build();

            objectMapper.writeValue(response.getOutputStream(), errorResponse);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private record RequestCounter(AtomicInteger count, long timestamp) {}
}
