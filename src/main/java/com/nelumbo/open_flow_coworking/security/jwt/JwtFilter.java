package com.nelumbo.open_flow_coworking.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelumbo.open_flow_coworking.exception.ApiExceptionError.ApiExceptionError;
import com.nelumbo.open_flow_coworking.model.response.ErrorResponse;
import com.nelumbo.open_flow_coworking.security.user.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (
                authHeader == null ||
                        !authHeader.startsWith("Bearer ") ||
                        request.getRequestURI().equals("/api/auth/login") ||
                        request.getRequestURI().equals("/api/auth/refresh")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            int code = 1004;
            String message = ApiExceptionError.formatMessage(code);

            handleError(response, message, code);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleError(HttpServletResponse response, String message, int code) throws IOException {
        ErrorResponse error = new ErrorResponse(message, HttpStatus.UNAUTHORIZED, code);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(error));
    }
}