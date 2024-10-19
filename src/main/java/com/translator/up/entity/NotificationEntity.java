package com.translator.up.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.translator.up.model.response.NotificationDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "message")
    private String message;
    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "has_read")
    private Boolean hasRead;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonBackReference
    private UserEntity userSender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @JsonBackReference
    private UserEntity userReceiver;
    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private ProjectEntity project;

    public NotificationDTO mapToDTO() {
        return new NotificationDTO(this.id, this.title, this.message, this.createdAt, this.hasRead, this.userSender.getFullName(), this.project.getTitle());
    }
}
