package edu.ccsu.cs492.kerberoschat.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "app_user", uniqueConstraints = {
        @UniqueConstraint(name = "app_user_pk", columnNames = "user_name")
})
public class AppUser {

    @Id
    @Column(name = "user_name", length = 36, nullable = false)
    String userName;

    @Column(name = "encrypted_password", length = 80, nullable = false)
    String password;

    @Column(name = "f_name", length = 30)
    String firstName;

    @Column(name = "l_name", length = 30)
    String lastName;

    @Column(name = "is_active", length = 1, nullable = false)
    Boolean isActive;
}
