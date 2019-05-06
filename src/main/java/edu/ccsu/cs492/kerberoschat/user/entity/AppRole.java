package edu.ccsu.cs492.kerberoschat.user.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * JPA class for user roles
 *
 * These roles are used in determining the access level of a user. The sets of these roles are not disjoint, ie an admin
 * can have all the roles to represent they're an admin
 */
@Data
@Entity
@Table(name = "app_role", uniqueConstraints = {
        @UniqueConstraint(name = "app_role_pk", columnNames = "role_id"),
        @UniqueConstraint(name = "app_role_uk", columnNames = "role_name")
})
public class AppRole {

    /**
     * The id of a role
     */
    @Id
    @Column(name = "role_id", nullable = false)
    Integer roleId;

    /**
     * A specific role for a user in the format ROLE_{ROLE_NAME}
     */
    @Column(name = "role_name", length = 30, nullable = false)
    String roleName;
}
