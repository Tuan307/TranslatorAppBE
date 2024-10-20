package com.translator.up.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.translator.up.model.response.ProjectDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "project")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "source_language")
    private String sourceLanguage;
    @Column(name = "target_language")
    private String targetLanguage;
    @Column(name = "budget")
    private Double budget;
    @Column(name = "deadline")
    private String deadline;
    @Column(name = "status")
    private String status;
    @Column(name = "translate_file")
    private String translateFile;
    @Column(name = "translated_file")
    private String translatedFile;
    @Column(name = "created_at")
    private String createdAt;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "translator_id")
    @JsonBackReference
    private UserEntity translator;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonManagedReference
    private List<NotificationEntity> notificationEntityList;
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private ReviewEntity review;

    public ProjectDTO mapToDTO() {
        if (translator == null) {
            return new ProjectDTO(
                    this.id, this.title, this.user.getFullName(), "", this.user.getPhoneNumber(), this.user.getEmail(),
                    this.description, this.sourceLanguage, this.targetLanguage, this.budget, this.deadline, this.status, this.translateFile, this.translatedFile,
                    this.createdAt
            );
        }
        return new ProjectDTO(
                this.id, this.title, this.user.getFullName(), String.valueOf(this.translator.getFullName()), this.user.getPhoneNumber(), this.user.getEmail(),
                this.description, this.sourceLanguage, this.targetLanguage, this.budget, this.deadline, this.status, this.translateFile, this.translatedFile,
                this.createdAt
        );
    }
}
