package com.translator.up.service.notification;

import com.translator.up.entity.NotificationEntity;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.response.NotificationDTO;
import com.translator.up.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public ApiResponse<List<NotificationDTO>> getAllPersonalNotification(Long userId) {
        List<NotificationEntity> notificationList = notificationRepository.findNotificationByUserId(userId);
        List<NotificationDTO> dtoList = new ArrayList<>();
        for (NotificationEntity entity : notificationList) {
            dtoList.add(entity.mapToDTO());
        }
        return new ApiResponse<>("success", "Successful", dtoList, "");
    }

    public ApiResponse<String> readNotification(Long id) {
        Optional<NotificationEntity> notificationEntity = notificationRepository.findById(id);
        if (notificationEntity.isPresent()) {
            NotificationEntity entity = notificationEntity.get();
            entity.setHasRead(true);
            notificationRepository.save(entity);
            return new ApiResponse<>("success", "Successful", "Successfully read notification", "");
        } else {
            return new ApiResponse<>("error", "Can not find notification", null, "400");
        }
    }

    public ApiResponse<String> deleteNotification(Long id) {
        Optional<NotificationEntity> notificationEntity = notificationRepository.findById(id);
        if (notificationEntity.isPresent()) {
            notificationRepository.deleteById(id);
            return new ApiResponse<>("success", "Successful", "Successfully remove notification", "");
        } else {
            return new ApiResponse<>("error", "Can not find notification", null, "400");
        }
    }
}
