package com.translator.up.service.user;

import com.translator.up.entity.ProjectEntity;
import com.translator.up.entity.UserEntity;
import com.translator.up.exception.user.EmailAlreadyExistsException;
import com.translator.up.exception.user.PhoneNumberAlreadyExistsException;
import com.translator.up.exception.user.UserDoesNotExistsException;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.request.ProjectRequest;
import com.translator.up.model.request.RegisterUserRequest;
import com.translator.up.model.request.UpdateUserRequest;
import com.translator.up.model.response.ProjectDTO;
import com.translator.up.repository.user.ProjectRepository;
import com.translator.up.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ProjectRepository projectRepository;

    public UserEntity registerUser(RegisterUserRequest request) {
        String encodePassword = passwordEncoder.encode(request.getPassword());
        Optional<UserEntity> checkingPhoneNumber = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (checkingPhoneNumber.isPresent()) {
            throw new PhoneNumberAlreadyExistsException("Phone number already exists");
        }

        Optional<UserEntity> checkEmail = userRepository.findByEmail(request.getEmail());
        if (checkEmail.isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(encodePassword);
        user.setRole(request.getRole());
        userRepository.save(user);
        return user;
    }

    public ApiResponse<UserEntity> login(String email, String password) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserDoesNotExistsException("User does not exist");
        }
        if (passwordEncoder.matches(password, user.get().getPassword())) {
            return new ApiResponse<UserEntity>("success", "Login successfully", user.get(), "200");
        } else {
            return new ApiResponse<UserEntity>("error", "Wrong password", null, "400");
        }
    }

    public ApiResponse<ProjectDTO> addProject(ProjectRequest request) {
        Optional<UserEntity> user = userRepository.findById(request.getClientId());
        if (user.isPresent()) {
            ProjectEntity projectEntity = new ProjectEntity();
            projectEntity.setBudget(request.getBudget());
            projectEntity.setDeadline(request.getDeadline());
            projectEntity.setDescription(request.getDescription());
            projectEntity.setTitle(request.getTitle());
            projectEntity.setSourceLanguage(request.getSourceLanguage());
            projectEntity.setTargetLanguage(request.getTargetLanguage());
            projectEntity.setCreatedAt(request.getCreatedAt());
            projectEntity.setTranslateFile(request.getFile());
            projectEntity.setStatus(request.getStatus());
            user.get().addProject(projectEntity);
            projectRepository.save(projectEntity);
            ProjectDTO projectDTO = projectEntity.mapToDTO();
            return new ApiResponse<>("success", "Success", projectDTO, "200");
        } else {
            throw new UserDoesNotExistsException("User does not exist");
        }
    }

    public ApiResponse<UserEntity> getUserProfile(Long userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        return user.map(userEntity -> new ApiResponse<>("success", "Success", userEntity, null)).orElseGet(() -> new ApiResponse<>("success", "User not found", null, "400"));
    }

    public ApiResponse<UserEntity> editUserProfile(UpdateUserRequest request) {
        Optional<UserEntity> user = userRepository.findById(request.getId());
        if (user.isEmpty()) {
            return new ApiResponse<>("success", "User not found", null, "400");
        } else {
            UserEntity editUser = user.get();
            editUser.setFullName(request.getFullName());
            editUser.setPhoneNumber(request.getPhoneNumber());
            editUser.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(editUser);
            return new ApiResponse<>("success", "User not found", editUser, null);
        }
    }
}
