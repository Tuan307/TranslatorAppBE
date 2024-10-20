package com.translator.up.repository.notification;

import com.translator.up.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @Query("SELECT n FROM NotificationEntity n WHERE n.userReceiver.id = :userId")
    List<NotificationEntity> findNotificationByUserId(@Param("userId") Long userId);
}
