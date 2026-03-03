package com.nelumbo.open_flow_coworking.service.Implementation;

import com.nelumbo.open_flow_coworking.Mappers.UserMapper;
import com.nelumbo.open_flow_coworking.entity.User;
import com.nelumbo.open_flow_coworking.repository.UserRepository;
import com.nelumbo.open_flow_coworking.service.UserService;
import com.nelumbo.open_flow_coworking.shared.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public Page<UserDto> searchUsers(String q, int limit, int page) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

        Page<User> users;

        if (q == null) {
            users = userRepository.findAllByRecycleFalse(pageable);
        } else {
            users = userRepository.findAllByRecycleFalse(pageable);
        }

        return users.map(userMapper::toDto);
    }

    @Override
    public UserDto userDetail(UUID userId) {
        User user = userRepository.findByIdAndRecycleFalse(userId)
                .orElseThrow(RuntimeException::new);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return null;
    }

    @Override
    public UserDto updateUser(UUID userId, UserDto userDto) {
        return null;
    }
}
