package com.nelumbo.open_flow_coworking.service.Implementation;

import com.nelumbo.open_flow_coworking.mappers.UserMapper;
import com.nelumbo.open_flow_coworking.entity.User;
import com.nelumbo.open_flow_coworking.exception.OpenFlowException;
import com.nelumbo.open_flow_coworking.repository.UserRepository;
import com.nelumbo.open_flow_coworking.security.utils.PasswordGenerator;
import com.nelumbo.open_flow_coworking.service.UserService;
import com.nelumbo.open_flow_coworking.shared.dto.UserDto;
import com.nelumbo.open_flow_coworking.shared.enums.UserRole;
import com.nelumbo.open_flow_coworking.util.CreatePageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordGenerator passwordGenerator;

    @Override
    public Page<UserDto> searchUsers(String q, int limit, int page) {
        Pageable pageable = CreatePageable.buildPageable(page, limit, "createdAt");

        Page<User> users;

        if (q == null) {
            users = userRepository.findAllByRecycleFalse(pageable);
        } else {
            users = userRepository.searchByQuery(q, pageable);
        }

        return users.map(userMapper::toDto);
    }

    @Override
    public UserDto userDetail(UUID userId) {
        User user = userRepository.findByIdAndRecycleFalse(userId)
                .orElseThrow(() -> new OpenFlowException(2, "User", "ID", userId.toString()));

        return userMapper.toDto(user);
    }

    @Override
    public UserDto createOperator(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new OpenFlowException(3, "User", "email", userDto.getEmail());
        }

        String salt = passwordGenerator.generateSalt();
        String passwordHash = passwordGenerator.hash(userDto.getPassword(), salt);

        User user = userMapper.toEntity(userDto);

        user.setSalt(salt);
        user.setPassword(passwordHash);
        user.setRole(UserRole.OPERATOR);

        user = userRepository.save(user);
        
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(UUID userId, UserDto userDto) {
        User user = userRepository.findByIdAndRecycleFalse(userId)
                .orElseThrow(() -> new OpenFlowException(2, "User", "ID", userId.toString()));

        if (
                userDto.getEmail() != null &&
                        !userDto.getEmail().equals(user.getEmail())
        ) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new OpenFlowException(3, "User", "email", userDto.getEmail());
            }

            user.setEmail(userDto.getEmail());
        }

        if (
                userDto.getName() != null &&
                        !userDto.getName().equals(user.getName())
        ) {
            user.setName(userDto.getName());
        }

        if (
                userDto.getPassword() != null
        ) {
            String salt = passwordGenerator.generateSalt();
            String passwordHash = passwordGenerator.hash(userDto.getPassword(), salt);

            user.setSalt(salt);
            user.setPassword(passwordHash);
        }

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = userRepository.findByIdAndRecycleFalse(userId)
                .orElseThrow(() -> new OpenFlowException(2, "User", "ID", userId.toString()));

        user.setRecycle(false);
        userRepository.save(user);
    }
}
