package edu.ccsu.cs492.kerberoschat.user.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * JPA for the user_role table in the database
 */
@Data
@Entity
@IdClass(UserRolePK.class)
@Table(name = "user_role", uniqueConstraints = {
        @UniqueConstraint(name = "user_role_pk", columnNames = {"user_name", "role_id"})
})
public class UserRole {

    /**
     * The user that possesses the role
     */
    @Id
    @Column(name = "user_name", length = 36, nullable = false)
    String username;

    /**
     * The role associated with a user
     */
    @Id
    @Column(name = "role_id", nullable = false)
    Integer roleId;

    /**
     * Object that contains information on the user with the above username
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private AppUser appUser;

    /**
     * Object that contains information on the role with the above roleId
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private AppRole appRole;
}
