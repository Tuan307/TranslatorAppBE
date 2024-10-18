package com.translator.up.service.user;

import com.translator.up.entity.ProjectEntity;
import com.translator.up.entity.UserEntity;
import com.translator.up.exception.user.EmailAlreadyExistsException;
import com.translator.up.exception.user.PhoneNumberAlreadyExistsException;
import com.translator.up.exception.user.UserDoesNotExistsException;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.request.ProjectRequest;
import com.translator.up.model.request.RegisterUserRequest;
import com.translator.up.model.request.UpdateTranslatedFileProject;
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
        if (request.getRole().equalsIgnoreCase("customer")) {
            user.setStatus("working");
        } else {
            user.setStatus("pending");
        }
        userRepository.save(user);
        return user;
    }

    public ApiResponse<UserEntity> login(String email, String password) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserDoesNotExistsException("User does not exist");
        }
        if (passwordEncoder.matches(password, user.get().getPassword())) {
            if (user.get().getStatus().equalsIgnoreCase("pending")) {
                return new ApiResponse<>("success", "Wait for admin to approve your account", null, "401");
            }
            return new ApiResponse<>("success", "Login successfully", user.get(), "200");
        } else {
            return new ApiResponse<>("error", "Wrong password", null, "400");
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

    public ApiResponse<ProjectDTO> updateTranslatedFileProject(UpdateTranslatedFileProject request) {
        Optional<UserEntity> user = userRepository.findById(request.getId());
        if (user.isPresent()) {
            ProjectEntity projectEntity = new ProjectEntity();
            projectEntity.setTranslateFile(request.getTranslatedFile());
            projectRepository.save(projectEntity);
            ProjectDTO projectDTO = projectEntity.mapToDTO();
            return new ApiResponse<>("success", "Success", projectDTO, "200");
        } else {
            throw new UserDoesNotExistsException("User does not exist");
        }
    }
}
