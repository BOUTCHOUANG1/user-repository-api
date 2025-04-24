package com.nathan.usermanagementapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * The User entity represents a user in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the user.
     */
    @NotBlank
    @Size(max = 50)
    private String name;

    /**
     * The email address of the user.
     */
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    /**
     * The password of the user.
     */
    @NotBlank
    @Size(max = 120)
    private String password;

    /**
     * The date when the user was created.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * The date when the user was last updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    /**
     * Sets the created date before persisting the user entity.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    /**
     * Updates the updated date before updating the user entity.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}