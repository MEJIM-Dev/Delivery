package com.me.profile_service.model;

import com.me.profile_service.model.enumeration.Gender;
import com.me.profile_service.model.enumeration.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class AppUser {

    @Id
    private long id;
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    @Column(nullable = false, length = 30, unique = true)
    private String mobile;
    @Column(nullable = false, length = 100, unique = true)
    private String password;
    @Column(nullable = false, length = 100)
    private String login;
    @Column(nullable = false, length = 50)
    private String firstname;
    @Column(nullable = false, length = 50)
    private String lastname;
    @Column(length = 50)
    private String otherNames;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender sex;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @Column(nullable = false, updatable = false)
    @CreatedDate()
    private Instant createdOn;
    @LastModifiedDate()
    @Column(insertable = false, updatable = true)
    private Instant updatedOn;
}
