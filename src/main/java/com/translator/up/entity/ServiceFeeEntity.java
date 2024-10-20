package com.translator.up.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "service_fee")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceFeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "source_language")
    private String sourceLanguage;
    @Column(name = "target_language")
    private String targetLanguage;
    @Column(name = "base_fee")
    private Double baseFee;
}
