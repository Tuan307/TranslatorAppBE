package com.translator.up.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.translator.up.model.response.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;
    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProjectEntity> projectEntityList;
    @OneToMany(mappedBy = "translator", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProjectEntity> translatorProjectList;
    @OneToMany(mappedBy = "userSender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonManagedReference
    private List<NotificationEntity> senderNotificationList;
    @OneToMany(mappedBy = "userReceiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<NotificationEntity> receiverNotificationList;
    @OneToMany(mappedBy = "userSupport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<SupportTicketEntity> supportTicketEntityList;

    public void addProject(ProjectEntity entity) {
        projectEntityList.add(entity);
        entity.setUser(this);
    }

    public void addTranslateProject(ProjectEntity entity) {
        translatorProjectList.add(entity);
        entity.setTranslator(this);
    }

    public void addNotifications(NotificationEntity entity) {
        receiverNotificationList.add(entity);
        entity.setUserReceiver(this);
    }

    public void removeProject(ProjectEntity entity) {
        projectEntityList.remove(entity);
        entity.setUser(null);
    }

    public UserDTO mapToDTO() {
        return new UserDTO(this.id, this.fullName, this.email, this.phoneNumber, this.role, this.status);
    }
}
