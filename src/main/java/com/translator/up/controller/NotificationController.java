package com.translator.up.controller;

import com.translator.up.aop.SessionRequired;
import com.translator.up.entity.UserEntity;
import com.translator.up.exception.user.SessionNotFoundException;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.response.NotificationDTO;
import com.translator.up.service.notification.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @SessionRequired
    @GetMapping("")
    public ApiResponse<List<NotificationDTO>> getAllPersonalNotification(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new SessionNotFoundException("Login required");
        }
        return notificationService.getAllPersonalNotification(user.getId());
    }

    @SessionRequired
    @PutMapping("/read/{id}")
    public ApiResponse<String> readNotification(@PathVariable(name = "id") Long id) {
        return notificationService.readNotification(id);
    }

    @SessionRequired
    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteNotification(@PathVariable(name = "id") Long id) {
        return notificationService.deleteNotification(id);
    }
}
