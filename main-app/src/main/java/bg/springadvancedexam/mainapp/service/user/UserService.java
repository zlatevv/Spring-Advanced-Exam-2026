package bg.springadvancedexam.mainapp.service.user;

import bg.springadvancedexam.mainapp.dto.auth.UserResponse;
import bg.springadvancedexam.mainapp.dto.user.ProfileResponse;
import bg.springadvancedexam.mainapp.dto.user.UpdateProfileRequest;
import bg.springadvancedexam.mainapp.exception.user.EmailExistsException;
import bg.springadvancedexam.mainapp.exception.user.LastAdminException;
import bg.springadvancedexam.mainapp.exception.user.UserNotFoundException;
import bg.springadvancedexam.mainapp.mapper.user.UserMapper;
import bg.springadvancedexam.mainapp.model.entity.user.User;
import bg.springadvancedexam.mainapp.model.enums.Role;
import bg.springadvancedexam.mainapp.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Cacheable(value = "userProfiles", key = "#userId")
    public ProfileResponse fetchMyProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found!"));

        return UserMapper.toProfileResponse(user);
    }

    @Transactional
    @CacheEvict(value = "userProfiles", key = "#userId")
    public ProfileResponse updateProfile(UUID userId, UpdateProfileRequest updateProfileRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found!"));

        String normalizedEmail = updateProfileRequest.email().trim().toLowerCase();

        if (!user.getEmail().equalsIgnoreCase(normalizedEmail)
                && userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailExistsException("Email already in use!");
        }

        user.setEmail(normalizedEmail);
        user.setFullName(updateProfileRequest.fullName());

        if (updateProfileRequest.institution() != null) {
            user.setInstitution(updateProfileRequest.institution());
        }

        User saved = userRepository.save(user);
        return UserMapper.toProfileResponse(saved);
    }

    @Cacheable(value = "allUsers", key = "'all'")
    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserResponse)
                .toList();
    }

    @Transactional
    @CacheEvict(value = "allUsers", key = "'all'")
    public UserResponse updateUserRole(UUID userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found!"));

        if (user.getRole() == Role.ADMIN
                && role != Role.ADMIN
                && userRepository.countByRole(Role.ADMIN) <= 1) {
            throw new LastAdminException("Cannot remove the last remaining admin.");
        }

        user.setRole(role);
        User saved = userRepository.save(user);

        return UserMapper.toUserResponse(saved);
    }
}
