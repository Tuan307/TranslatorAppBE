package com.translator.up.controller;

import com.translator.up.aop.SessionRequired;
import com.translator.up.entity.ProjectEntity;
import com.translator.up.entity.UserEntity;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.request.*;
import com.translator.up.model.response.ProjectDTO;
import com.translator.up.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

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
    @PutMapping("/project/edit")
    public ApiResponse<ProjectEntity> editTranslateProject(@RequestBody UpdateTranslateProjectRequest request) {
        return userService.editTranslateProject(request);
    }

    @SessionRequired
    @PostMapping("/project/create")
    public ApiResponse<ProjectDTO> createTranslateRequest(@RequestBody ProjectRequest request) {
        return userService.addProject(request);
    }

    @SessionRequired
    @GetMapping("/project")
    public ApiResponse<ProjectEntity> getTranslateProject(@RequestParam(name = "id") Long id) {
        return userService.getTranslateProject(id);
    }

    @SessionRequired
    @DeleteMapping("/project/delete")
    public ApiResponse<ProjectEntity> deleteTranslateProject(@RequestParam(name = "id") Long id) {
        return userService.deleteTranslateProject(id);
    }

}
