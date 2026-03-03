package com.nelumbo.open_flow_coworking.exception.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelumbo.open_flow_coworking.exception.ApiExceptionError.ApiExceptionError;
import com.nelumbo.open_flow_coworking.model.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntrypoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            @NonNull AuthenticationException authException
    ) throws IOException {
        int code = 1004;

        String message = ApiExceptionError.formatMessage(code);
        HttpStatus status = HttpStatus.valueOf(ApiExceptionError.getHttpStatusName(code));

        ErrorResponse error = new ErrorResponse(message, status, code);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(error));
    }
}
