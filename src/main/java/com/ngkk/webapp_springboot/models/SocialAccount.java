package com.ngkk.webapp_springboot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "social_accounts")
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider",nullable = false,length = 20)
    private String provider;

    @Column(name = "provider_id",nullable = false,length = 50)
    private String providerId;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "name",length = 150)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

}
