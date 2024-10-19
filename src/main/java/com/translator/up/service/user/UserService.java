package com.translator.up.service.user;

import com.translator.up.entity.ProjectEntity;
import com.translator.up.entity.UserEntity;
import com.translator.up.exception.user.EmailAlreadyExistsException;
import com.translator.up.exception.user.PhoneNumberAlreadyExistsException;
import com.translator.up.exception.user.UserDoesNotExistsException;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.request.*;
import com.translator.up.model.response.ProjectDTO;
import com.translator.up.repository.user.ProjectRepository;
import com.translator.up.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ProjectRepository projectRepository;

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public UserService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory to store files.", ex);
        }
    }

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
            if (user.get().getStatus() != null && user.get().getStatus().equalsIgnoreCase("pending")) {
                return new ApiResponse<>("success", "Wait for admin to approve your account", null, "401");
            }
            return new ApiResponse<>("success", "Login successfully", user.get(), "200");
        } else {
            return new ApiResponse<>("error", "Wrong password", null, "400");
        }
    }

    public ApiResponse<ProjectDTO> addProject(ProjectRequest request) {
        Optional<UserEntity> user = userRepository.findById(request.getClientId());
        Long id = (long) projectRepository.findAll().size() + 1;
        if (user.isPresent()) {
            try {
                ProjectEntity projectEntity = new ProjectEntity();
                projectEntity.setBudget(request.getBudget());
                projectEntity.setDeadline(request.getDeadline());
                projectEntity.setDescription(request.getDescription());
                projectEntity.setTitle(request.getTitle());
                projectEntity.setSourceLanguage(request.getSourceLanguage());
                projectEntity.setTargetLanguage(request.getTargetLanguage());
                projectEntity.setCreatedAt(request.getCreatedAt());
                String translateFilePath = storeFile(request.getFile(), id);
                projectEntity.setTranslateFile(translateFilePath);
                projectEntity.setStatus(request.getStatus());
                user.get().addProject(projectEntity);
                projectRepository.save(projectEntity);
                ProjectDTO projectDTO = projectEntity.mapToDTO();
                return new ApiResponse<>("success", "Success", projectDTO, "200");
            } catch (IOException e) {
                return new ApiResponse<>("error", "Error converting file", null, "400");
            }
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

    public ApiResponse<UserEntity> getUserProfile(Long userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        return user.map(userEntity -> new ApiResponse<>("success", "Success", userEntity, null)).orElseGet(() -> new ApiResponse<>("success", "User not found", null, "400"));
    }

    public ApiResponse<UserEntity> editUserProfile(UpdateUserRequest request) {
        Optional<UserEntity> user = userRepository.findById(request.getId());
        if (user.isEmpty()) {
            return new ApiResponse<>("error", "User not found", null, "400");
        } else {
            UserEntity editUser = user.get();
            editUser.setFullName(request.getFullName());
            editUser.setPhoneNumber(request.getPhoneNumber());
            editUser.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(editUser);
            return new ApiResponse<>("success", "Successfully", editUser, null);
        }
    }

    public ApiResponse<ProjectEntity> editTranslateProject(UpdateTranslateProjectRequest request) {
        Optional<ProjectEntity> project = projectRepository.findById(request.getId());
        if (project.isEmpty()) {
            return new ApiResponse<>("error", "Project not found", null, "400");
        } else {
            try {
                ProjectEntity projectEntity = project.get();
                projectEntity.setTitle(request.getTitle());
                projectEntity.setDescription(request.getDescription());
                projectEntity.setDeadline(request.getDeadline());
                projectEntity.setBudget(request.getBudget());
                projectEntity.setSourceLanguage(request.getSourceLanguage());
                projectEntity.setTargetLanguage(request.getTargetLanguage());
                String translateFilePath = storeFile(request.getTranslateFile(), projectEntity.getId());
                projectEntity.setTranslateFile(translateFilePath);
                projectRepository.save(projectEntity);
                return new ApiResponse<>("success", "Successfully", projectEntity, null);
            } catch (IOException e) {
                return new ApiResponse<>("error", "File not found", null, "400");
            }
        }
    }

    public ApiResponse<ProjectDTO> getTranslateProject(Long id) {
        Optional<ProjectEntity> project = projectRepository.findById(id);
        if (project.isEmpty()) {
            return new ApiResponse<>("error", "Project not found", null, "400");
        } else {
            ProjectDTO dto = project.get().mapToDTO();
            return new ApiResponse<>("success", "Successfully", dto, null);
        }
    }

    public ApiResponse<List<ProjectDTO>> getAllTranslateProject() {
        List<ProjectEntity> project = projectRepository.findAll();
        ;
        if (project.isEmpty()) {
            return new ApiResponse<>("error", "Project not found", null, "400");
        } else {
            ArrayList<ProjectDTO> result = new ArrayList<>();
            for (ProjectEntity i : project) {
                ProjectDTO dto = i.mapToDTO();
                result.add(dto);
            }
            return new ApiResponse<>("success", "Successfully", result, null);
        }
    }

    public ApiResponse<List<ProjectDTO>> getAllPersonalTranslateProject(String status, Long userID) {
        List<ProjectEntity> project = projectRepository.findByStatusAndUserId(status, userID);
        if (project.isEmpty()) {
            return new ApiResponse<>("error", "Project not found", null, "400");
        } else {
            ArrayList<ProjectDTO> result = new ArrayList<>();
            for (ProjectEntity i : project) {
                ProjectDTO dto = i.mapToDTO();
                result.add(dto);
            }
            return new ApiResponse<>("success", "Successfully", result, null);
        }
    }

    public ApiResponse<ProjectEntity> deleteTranslateProject(Long id) {
        Optional<ProjectEntity> project = projectRepository.findById(id);
        if (project.isEmpty()) {
            return new ApiResponse<>("error", "Project not found", null, "400");
        } else {
            projectRepository.deleteById(project.get().getId());
            return new ApiResponse<>("success", "Successfully", null, null);
        }
    }

    // luu tru file
    private String storeFile(MultipartFile file, Long projectId) throws IOException {
        // Normalize file name and save it
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        Path subFolder = this.fileStorageLocation.resolve(String.valueOf(projectId));
        if (!Files.exists(subFolder)) {
            Files.createDirectories(subFolder);
        }
        Path targetLocation = subFolder.resolve(fileName);
        if (Files.exists(targetLocation)) {
            Files.delete(targetLocation);
        }
        Files.copy(file.getInputStream(), targetLocation);
        return fileName;
    }

    // Tạo URI cho file download
    private String createDownloadUri(String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/v1/user/download/project/")
                .path(fileName)
                .toUriString();
    }

    // Tải file từ hệ thống
    public Resource loadFileAsResource(Long projectId, String fileName) {
        try {
            Path subFolder = this.fileStorageLocation.resolve(String.valueOf(projectId));
            if (!Files.exists(subFolder)) {
                throw new RuntimeException("File not found " + fileName);
            }
            Path targetLocation = subFolder.resolve(fileName).normalize();
            Resource resource = new UrlResource(targetLocation.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }

    // Xoa file khoi storage
    public void deleteFile(String fileName) throws IOException {
        // Construct the file path
        Path filePath = this.fileStorageLocation.resolve(fileName);
        // Delete the file if it exists
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        } else {
            throw new IOException("File not found: " + fileName);
        }
    }
}
