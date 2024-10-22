package com.translator.up.entity;

import com.translator.up.model.response.SupportTicketDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "support_ticket")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupportTicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userSupport;

    public SupportTicketDTO mapToDTO() {
        return new SupportTicketDTO(this.id, this.title, this.description, this.userSupport.getId(), this.userSupport.getFullName(), this.userSupport.getEmail(), this.userSupport.getPhoneNumber(), this.userSupport.getRole());
    }
}
