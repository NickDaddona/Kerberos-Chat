package edu.ccsu.cs492.kerberoschat.user.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * JPA class for Users, directly maps to app_user table in the database
 */
@Data
@Entity
@Table(name = "app_user", uniqueConstraints = {
        @UniqueConstraint(name = "app_user_pk", columnNames = "user_name")
})
public class AppUser {

    /**
     * The Username for  user, the primary key of the table
     */
    @Id
    @Column(name = "user_name", length = 36, nullable = false)
    String userName;

    /**
     * The password for user, hashed with salt appended
     */
    @Column(name = "encrypted_password", length = 80, nullable = false)
    String password;

    /**
     * The first name for user
     */
    @Column(name = "f_name", length = 30)
    String firstName;

    /**
     * The last name for user
     */
    @Column(name = "l_name", length = 30)
    String lastName;

    /**
     * Flag that if set to true allows a user to log in.
     * Can be used to lockout their account to prevent unauthorized access
     */
    @Column(name = "is_active", length = 1, nullable = false)
    Boolean isActive;
}
