package com.translator.up.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.translator.up.aop.SessionRequired;
import com.translator.up.entity.ProjectEntity;
import com.translator.up.entity.UserEntity;
import com.translator.up.exception.user.SessionNotFoundException;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.request.AdminUpdateUserRequest;
import com.translator.up.model.request.ApproveUserRequest;
import com.translator.up.model.request.LoginRequest;
import com.translator.up.model.request.ProjectRequest;
import com.translator.up.model.request.RegisterUserRequest;
import com.translator.up.model.request.TranslatorAcceptTranslateProject;
import com.translator.up.model.request.UpdateTranslateProjectRequest;
import com.translator.up.model.request.UpdateTranslatedFileProject;
import com.translator.up.model.request.UpdateUserRequest;
import com.translator.up.model.response.ProjectDTO;
import com.translator.up.model.response.UserDTO;
import com.translator.up.service.user.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    @PostConstruct
    private void initFileUploadsStorage() {
        File uploadDir = new File("uploads");

        // Kiểm tra nếu thư mục chưa tồn tại, thì tạo nó
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(@RequestBody RegisterUserRequest request) {
        UserEntity user = userService.registerUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ApiResponse<UserEntity> login(@RequestBody LoginRequest request, HttpSession session) {
        ApiResponse<UserEntity> user = userService.login(request.getEmail(), request.getPassword());
        if (!user.getErrorCode().equals("401")) {
            session.setAttribute("user", user.getData());
        }
        return user;
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpSession session) {
        session.invalidate();
        return new ApiResponse<>("success", "Successfully logout", null, "200");
    }

    @GetMapping("/status")
    public ApiResponse<UserEntity> status(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return new ApiResponse<>("success", "No auto login", null, "400");
        } else {
            return new ApiResponse<>("success", "Auto login", user, "400");
        }
    }

    @SessionRequired
    @GetMapping("/list-user")
    public ApiResponse<List<UserEntity>> getListUser() {
        return userService.findUsers();
    }

    @SessionRequired
    @DeleteMapping("/delete-user")
    public ApiResponse<UserEntity> deleteuser(@RequestParam(name = "id") Long id) {
        return userService.deleteUser(id);
    }

    @SessionRequired
    @GetMapping("/profile")
    public ApiResponse<UserEntity> getUserProfile(@RequestParam("userId") Long userId) {
        return userService.getUserProfile(userId);
    }

    @SessionRequired
    @PutMapping("/profile/edit")
    public ApiResponse<UserEntity> editUserProfile(@RequestBody UpdateUserRequest request) {
        return userService.editUserProfile(request);
    }

    @SessionRequired
    @PutMapping("/edit-user")
    public ApiResponse<UserEntity> editUser(@RequestBody AdminUpdateUserRequest request) {
        return userService.editUser(request);
    }

    @SessionRequired
    @PutMapping(value = "/project/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProjectEntity> editTranslateProject(@RequestParam("id") Long id,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "sourceLanguage", required = false) String sourceLanguage,
            @RequestParam(name = "targetLanguage", required = false) String targetLanguage,
            @RequestParam(name = "budget", required = false) Double budget,
            @RequestParam(name = "deadline", required = false) String deadline,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "file", required = false) MultipartFile file) {
        UpdateTranslateProjectRequest request = new UpdateTranslateProjectRequest(id, title, description,
                sourceLanguage,
                targetLanguage, budget, deadline, status, file);

        return userService.editTranslateProject(request);
    }

    @SessionRequired
    @PostMapping(value = "/project/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProjectDTO> createTranslateRequest(@RequestParam("title") String title,
            @RequestParam("clientId") Long clientId,
            @RequestParam("description") String description,
            @RequestParam("sourceLanguage") String sourceLanguage,
            @RequestParam("targetLanguage") String targetLanguage,
            @RequestParam("budget") Double budget,
            @RequestParam("deadline") String deadline,
            @RequestParam("status") String status,
            @RequestParam(name = "file", required = false) MultipartFile file) {
        ProjectRequest request = new ProjectRequest(title, clientId, description, sourceLanguage, targetLanguage,
                budget, deadline, status, file, "");
        return userService.addProject(request);
    }

    @SessionRequired
    @GetMapping("/project")
    public ApiResponse<ProjectDTO> getTranslateProject(@RequestParam(name = "id") Long id) {
        return userService.getTranslateProject(id);
    }

    @GetMapping("/project/all")
    public ApiResponse<List<ProjectDTO>> getAllTranslateProject() {
        return userService.getAllTranslateProject();
    }

    @SessionRequired
    @GetMapping("/project/personal")
    public ApiResponse<List<ProjectDTO>> getAllPersonalTranslateProject(@RequestParam(name = "status") String status,
            HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (status != null && user == null) {
            throw new SessionNotFoundException("Login required");
        }
        return userService.getAllPersonalTranslateProject(status, user.getId());
    }

    // API để tải file từ server
    @GetMapping("/download/project/{projectId}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long projectId, @PathVariable String fileName) {
        // Lấy file từ service
        Resource resource = userService.downloadFile(projectId, fileName);
        if (!resource.exists()) {
            return ResponseEntity.badRequest().body(null);
        }
        // Trả về file dưới dạng ResponseEntity với file name
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @SessionRequired
    @DeleteMapping("/project/delete")
    public ApiResponse<ProjectEntity> deleteTranslateProject(@RequestParam(name = "id") Long id) {
        return userService.deleteTranslateProject(id);
    }

    @SessionRequired
    @GetMapping("/translators")
    public ApiResponse<List<UserEntity>> getAllTranslator() {
        return userService.findTranslators();
    }

    @SessionRequired
    @PutMapping("/translated/upload")
    public ApiResponse<ProjectDTO> uploadTranslatedFile(@RequestParam(name = "id") Long id,
            @RequestParam(name = "translated_file") MultipartFile translatedFile,
            @RequestParam(name = "status") String status, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        return userService
                .updateTranslatedFileProject(new UpdateTranslatedFileProject(id, user.getId(), status, translatedFile));
    }

    @SessionRequired
    @PutMapping("/translated/accept")
    public ApiResponse<ProjectDTO> acceptTranslateProject(@RequestBody TranslatorAcceptTranslateProject request,
            HttpSession session) {
        return userService.translatorAcceptTranslateRequest(request, request.getUserId());
    }

    @SessionRequired
    @GetMapping("/project/translator")
    public ApiResponse<List<ProjectEntity>> findProjectByLanguage(@RequestParam("sourceLang") String sourceLang,
            @RequestParam("targetLang") String targetLang) {
        return userService.findProjectByLanguage(sourceLang, targetLang);
    }

    @GetMapping("/admin/approve/list")
    public ApiResponse<List<UserDTO>> findUserForAdminToApprove() {
        return userService.findUserForAdminToApprove();
    }

    @PutMapping("/admin/approve")
    public ApiResponse<UserDTO> updateUserAccountStatus(@RequestBody ApproveUserRequest request) {
        return userService.updateUserAccountStatus(request);
    }
}
