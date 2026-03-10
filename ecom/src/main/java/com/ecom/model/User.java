package com.ecom.model;

import com.ecom.enums.UserRole;
import com.ecom.dto.Address;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "db_test")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "key_cloak_id")
    private String keyCloakId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "user_role")
    private UserRole userRole = UserRole.CUSTOMER;

    @Column(name = "address")
    @JdbcTypeCode(SqlTypes.JSON)
    private Address address;

    @CreationTimestamp
    @Column(name = "created_timestamp")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_timestamp")
    private LocalDateTime updatedAt;
}
