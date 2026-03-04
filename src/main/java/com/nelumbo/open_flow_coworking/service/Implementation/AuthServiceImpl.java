package com.nelumbo.open_flow_coworking.service.Implementation;

import com.nelumbo.open_flow_coworking.entity.User;
import com.nelumbo.open_flow_coworking.exception.OpenFlowException;
import com.nelumbo.open_flow_coworking.mappers.UserMapper;
import com.nelumbo.open_flow_coworking.repository.UserRepository;
import com.nelumbo.open_flow_coworking.security.jwt.JwtService;
import com.nelumbo.open_flow_coworking.security.utils.PasswordGenerator;
import com.nelumbo.open_flow_coworking.service.AuthService;
import com.nelumbo.open_flow_coworking.shared.dto.AuthDto;
import com.nelumbo.open_flow_coworking.shared.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordGenerator passwordGenerator;
    private final UserMapper userMapper;

    @Override
    public AuthDto login(AuthDto request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new OpenFlowException(2, "User", "email", request.email()));

        boolean isPasswordValid = passwordGenerator.verify(
                request.password(),
                user.getSalt(),
                user.getPassword()
        );

        if (!isPasswordValid) {
            throw new OpenFlowException(1006);
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return buildAuthResponse(user);
    }

    @Override
    public AuthDto refreshToken(String authHeader, String refreshToken) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new OpenFlowException(1001);
        }

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new OpenFlowException(1007);
        }

        String userEmail = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new OpenFlowException(2, "User", "email", userEmail));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new OpenFlowException(1003);
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return buildAuthResponse(user);
    }


    @Override
    public UserDto getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (
                authentication == null ||
                        !authentication.isAuthenticated() ||
                        Objects.equals(authentication.getPrincipal(), "anonymousUser")
        ) {
            throw new OpenFlowException(1002);
        }

        User user = (User) authentication.getPrincipal();
        return userMapper.toDto(user);
    }

    private AuthDto buildAuthResponse(User user) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthDto(
                accessToken,
                refreshToken,
                jwtService.getExpirationInMillis(),
                null,
                null
        );
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
