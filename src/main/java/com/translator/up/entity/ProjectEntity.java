package com.translator.up.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.translator.up.model.response.ProjectDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public ProjectDTO mapToDTO() {
        return new ProjectDTO(
                this.id, this.title, this.user.getFullName(), this.user.getPhoneNumber(), this.user.getEmail(),
                this.description, this.sourceLanguage, this.targetLanguage, this.budget, this.deadline, this.status, this.translateFile, this.translatedFile,
                this.createdAt
        );
    }
}
