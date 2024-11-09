package com.translator.up.service.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.translator.up.entity.NotificationEntity;
import com.translator.up.entity.ProjectEntity;
import com.translator.up.entity.UserEntity;
import com.translator.up.exception.user.EmailAlreadyExistsException;
import com.translator.up.exception.user.PhoneNumberAlreadyExistsException;
import com.translator.up.exception.user.UserDoesNotExistsException;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.request.AdminUpdateUserRequest;
import com.translator.up.model.request.ApproveUserRequest;
import com.translator.up.model.request.ProjectRequest;
import com.translator.up.model.request.RegisterUserRequest;
import com.translator.up.model.request.TranslatorAcceptTranslateProject;
import com.translator.up.model.request.UpdateTranslateProjectRequest;
import com.translator.up.model.request.UpdateTranslatedFileProject;
import com.translator.up.model.request.UpdateUserRequest;
import com.translator.up.model.response.ProjectDTO;
import com.translator.up.model.response.UserDTO;
import com.translator.up.repository.notification.NotificationRepository;
import com.translator.up.repository.user.ProjectRepository;
import com.translator.up.repository.user.UserRepository;
import com.translator.up.utils.FileUtils;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    private FileUtils fileUtilsClass = new FileUtils();

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
            // if (user.get().getStatus() != null &&
            // user.get().getStatus().equalsIgnoreCase("pending")) {
            // return new ApiResponse<>("success", "Wait for admin to approve your account",
            // null, "401");
            // }
            return new ApiResponse<>("success", "Login successfully", user.get(), "200");
        } else {
            return new ApiResponse<>("error", "Wrong password", null, "400");
        }
    }

    public ApiResponse<ProjectDTO> addProject(ProjectRequest request) {
        Optional<UserEntity> user = userRepository.findById(request.getClientId());
        List<ProjectEntity> projects = projectRepository.findAll();
        Long id = !projects.isEmpty() && projects.size() > 0 ? projects.get(projects.size() - 1).getId() + 1 : 1;
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
                String translateFilePath = fileUtilsClass.storeFile(request.getFile(), id);
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
        Optional<ProjectEntity> project = projectRepository.findById(request.getId());
        Optional<UserEntity> user = userRepository.findById(request.getUserUploadId());
        if (project.isPresent()) {
            try {
                ProjectEntity projectEntity = project.get();
                projectEntity.setStatus(request.getStatus());
                projectEntity.setTranslatedFile(
                        fileUtilsClass.storeFile(request.getTranslatedFile(), projectEntity.getId()));
                user.get().addTranslateProject(projectEntity);
                NotificationEntity notificationEntity = new NotificationEntity();
                notificationEntity.setTitle("Thông báo hoàn thành");
                notificationEntity
                        .setMessage(user.get().getFullName() + " đã dịch xong yêu cầu " + projectEntity.getTitle());
                notificationEntity.setHasRead(false);
                notificationEntity.setProject(projectEntity);
                notificationEntity.setCreatedAt("");
                notificationEntity.setUserSender(projectEntity.getTranslator());
                projectEntity.getUser().addNotifications(notificationEntity);
                projectRepository.save(projectEntity);
                notificationRepository.save(notificationEntity);
                ProjectDTO projectDTO = projectEntity.mapToDTO();
                return new ApiResponse<>("success", "Success", projectDTO, "200");
            } catch (IOException e) {
                return new ApiResponse<>("success", "Success", null, "400");
            }
        } else {
            return new ApiResponse<>("success", "Success", null, "400");
        }
    }

    public ApiResponse<ProjectDTO> translatorAcceptTranslateRequest(TranslatorAcceptTranslateProject request,
            Long userId) {
        Optional<ProjectEntity> project = projectRepository.findById(request.getId());
        Optional<UserEntity> user = userRepository.findById(userId);
        if (project.isPresent() && user.isPresent()) {
            ProjectEntity projectEntity = project.get();
            projectEntity.setStatus(request.getStatus());
            user.get().addTranslateProject(projectEntity);
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setTitle("Thông báo");
            notificationEntity.setMessage(user.get().getFullName() + " đã nhận yêu cầu " + projectEntity.getTitle());
            notificationEntity.setHasRead(false);
            notificationEntity.setProject(projectEntity);
            notificationEntity.setCreatedAt("");
            notificationEntity.setUserSender(projectEntity.getTranslator());
            projectEntity.getUser().addNotifications(notificationEntity);
            projectRepository.save(projectEntity);
            notificationRepository.save(notificationEntity);
            ProjectDTO projectDTO = projectEntity.mapToDTO();
            return new ApiResponse<>("success", "Success", projectDTO, "200");
        } else {
            return new ApiResponse<>("success", "Success", null, "400");
        }
    }

    public ApiResponse<UserEntity> getUserProfile(Long userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        return user.map(userEntity -> new ApiResponse<>("success", "Success", userEntity, null))
                .orElseGet(() -> new ApiResponse<>("success", "User not found", null, "400"));
    }

    public ApiResponse<UserEntity> editUserProfile(UpdateUserRequest request) {
        Optional<UserEntity> user = userRepository.findById(request.getId());
        if (user.isEmpty()) {
            return new ApiResponse<>("error", "User not found", null, "400");
        } else {
            UserEntity editUser = user.get();
            editUser.setFullName(request.getFullName());
            editUser.setPhoneNumber(request.getPhoneNumber());
            editUser.setEmail(request.getEmail());
            userRepository.save(editUser);
            return new ApiResponse<>("success", "Successfully", editUser, null);
        }
    }

    public ApiResponse<UserEntity> editUser(AdminUpdateUserRequest request) {
        Optional<UserEntity> user = userRepository.findById(request.getId());
        if (user.isEmpty()) {
            return new ApiResponse<>("error", "User not found", null, "400");
        } else {

            UserEntity editUser = user.get();
            editUser.setFullName(request.getFullName());
            editUser.setPhoneNumber(request.getPhoneNumber());
            editUser.setEmail(request.getEmail());
            editUser.setRole(request.getRole());
            if (request.getPassword() != null) {
                String encodePassword = passwordEncoder.encode(request.getPassword());
                editUser.setPassword(encodePassword);
            }

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
                if (request.getTitle() != null) {

                    projectEntity.setTitle(request.getTitle());
                }
                if (request.getDescription() != null) {

                    projectEntity.setDescription(request.getDescription());
                }
                if (request.getDeadline() != null) {

                    projectEntity.setDeadline(request.getDeadline());
                }
                if (request.getBudget() != null) {

                    projectEntity.setBudget(request.getBudget());
                }
                if (request.getSourceLanguage() != null) {

                    projectEntity.setSourceLanguage(request.getSourceLanguage());
                }
                if (request.getStatus() != null) {

                    projectEntity.setStatus(request.getStatus());
                }
                if (request.getTargetLanguage() != null) {

                    projectEntity.setTargetLanguage(request.getTargetLanguage());
                }

                if (request.getTranslateFile() == null) {
                    projectEntity.setTranslateFile(project.get().getTranslateFile());

                } else {
                    String translateFilePath = fileUtilsClass.storeFile(request.getTranslateFile(),
                            projectEntity.getId());
                    projectEntity.setTranslateFile(translateFilePath);
                }
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

    public ApiResponse<UserEntity> deleteUser(Long id) {
        Optional<UserEntity> project = userRepository.findById(id);
        if (project.isEmpty()) {
            return new ApiResponse<>("error", "user not found", null, "400");
        } else {
            userRepository.deleteById(project.get().getId());
            return new ApiResponse<>("success", "Successfully", null, null);
        }
    }

    public Resource downloadFile(Long projectId, String fileName) {
        return fileUtilsClass.loadFileAsResource(projectId, fileName);
    }

    public ApiResponse<List<UserEntity>> findTranslators() {
        List<UserEntity> listOfTranslators = userRepository.findByRole("translator");
        return new ApiResponse<>("success", "Successful", listOfTranslators, "");
    }

    public ApiResponse<List<UserEntity>> findUsers() {
        List<UserEntity> listOfTranslators = userRepository.findAll();
        return new ApiResponse<>("success", "Successful", listOfTranslators, "");
    }

    public ApiResponse<List<ProjectEntity>> findProjectByLanguage(String source, String target) {
        List<ProjectEntity> list = projectRepository.findBySourceAndTargetLanguage(source, target);
        return new ApiResponse<>("success", "Successful", list, "");
    }

    public ApiResponse<List<UserDTO>> findUserForAdminToApprove() {
        return new ApiResponse<>("success", "Successful",
                userRepository.findByStatus("pending").stream().map(UserEntity::mapToDTO).collect(Collectors.toList()),
                "");
    }

    public ApiResponse<UserDTO> updateUserAccountStatus(ApproveUserRequest request) {
        Optional<UserEntity> user = userRepository.findById(request.getId());
        if (user.isEmpty()) {
            return new ApiResponse<>("error", "User not found", null, "400");
        }
        UserEntity model = user.get();
        if (request.getApproveUser()) {
            model.setStatus("working");
            userRepository.save(model);
        } else {
            userRepository.deleteById(model.getId());
        }
        return new ApiResponse<>("success", "Successful", model.mapToDTO(), "");
    }
}
