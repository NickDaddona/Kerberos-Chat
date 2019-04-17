package edu.ccsu.cs492.kerberoschat.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "app_role", uniqueConstraints = {
        @UniqueConstraint(name = "app_role_pk", columnNames = "role_id"),
        @UniqueConstraint(name = "app_role_uk", columnNames = "role_name")
})
public class AppRole {

    @Id
    @Column(name = "role_id", nullable = false)
    Integer roleId;

    @Column(name = "role_name", length = 30, nullable = false)
    String roleName;
}
