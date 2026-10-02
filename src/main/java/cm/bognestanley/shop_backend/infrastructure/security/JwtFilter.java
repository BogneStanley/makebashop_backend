package cm.bognestanley.shop_backend.infrastructure.security;

import java.io.IOException;
import java.util.Optional;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final AuthCookieService authCookieService;

    public JwtFilter(JwtUtils jwtUtils, UserRepository userRepository, AuthCookieService authCookieService) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.authCookieService = authCookieService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token == null) {
            log.debug("No authentication credential: method={}, path={}, bearerHeaderPresent={}, authCookiePresent={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    hasBearerToken(request),
                    hasAuthCookie(request));
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String email = jwtUtils.extractEmail(token);
            Long userId = jwtUtils.extractUserId(token);

            if (email == null || userId == null) {
                log.debug("JWT rejected: missing user claims, method={}, path={}", request.getMethod(), request.getRequestURI());
            } else if (SecurityContextHolder.getContext().getAuthentication() == null) {
                Optional<User> user = userRepository.findById(userId);
                if (user.isEmpty()) {
                    log.debug("JWT rejected: userId={} no longer exists", userId);
                } else if (!user.get().isActivate()) {
                    log.debug("JWT rejected: userId={} is inactive", userId);
                } else if (!user.get().getEmail().equals(email)) {
                    log.debug("JWT rejected: userId={} email claim does not match", userId);
                } else {
                    UserDetailsImpl userDetails = UserDetailsImpl.from(user.get());
                    if (!jwtUtils.isTokenValid(token, userDetails)) {
                        log.debug("JWT rejected: userId={} token validation failed", userId);
                    } else {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        log.debug("JWT authenticated: userId={}, authorities={}, method={}, path={}",
                                userId,
                                userDetails.getAuthorities(),
                                request.getMethod(),
                                request.getRequestURI());
                    }
                }
            }
        } catch (JwtException e) {
            log.debug("JWT rejected: {}, method={}, path={}", e.getClass().getSimpleName(), request.getMethod(), request.getRequestURI());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        String cookieName = authCookieService.getCookieName();
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isBlank()) {
                return cookie.getValue();
            }
        }

        return null;
    }

    private boolean hasBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private boolean hasAuthCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return false;
        }
        String cookieName = authCookieService.getCookieName();
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isBlank()) {
                return true;
            }
        }
        return false;
    }
}
