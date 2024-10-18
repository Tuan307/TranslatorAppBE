package com.translator.up.controller;

import com.translator.up.entity.UserEntity;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.request.LoginRequest;
import com.translator.up.model.request.ProjectRequest;
import com.translator.up.model.request.RegisterUserRequest;
import com.translator.up.model.request.UpdateUserRequest;
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

    @GetMapping("/status")
    public ApiResponse<UserEntity> status(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return new ApiResponse<>("success", "No auto login", null, "400");
        } else {
            return new ApiResponse<>("success", "Auto login", user, "400");
        }
    }

    @GetMapping("/profile")
    public ApiResponse<UserEntity> getUserProfile(@RequestParam("userId") Long userId, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return new ApiResponse<>("error", "Login required", null, "401");
        }
        return userService.getUserProfile(userId);
    }

    @PutMapping("/profile/edit")
    public ApiResponse<UserEntity> editUserProfile(@RequestBody UpdateUserRequest request, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return new ApiResponse<>("error", "Login required", null, "401");
        }
        return userService.editUserProfile(request);
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpSession session) {
        session.invalidate();
        return new ApiResponse<>("success", "Successfully logout", null, "200");
    }

    @PostMapping("/create/translaterequest")
    public ApiResponse<ProjectDTO> createTranslateRequest(@RequestBody ProjectRequest request, HttpSession httpSession) {
        UserEntity user = (UserEntity) httpSession.getAttribute("user");
        if (user == null) {
            return new ApiResponse<>("error", "Login required", null, "401");
        }
        return userService.addProject(request);
    }
}
