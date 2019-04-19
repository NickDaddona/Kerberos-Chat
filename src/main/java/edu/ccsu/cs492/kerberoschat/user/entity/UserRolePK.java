package edu.ccsu.cs492.kerberoschat.user.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Class for the Primary Key of the user_role table
 */
@Data
@AllArgsConstructor
public class UserRolePK implements Serializable {
    /**
     * The username of the user
     */
    private String userName;

    /**
     * A role of the user
     */
    private int roleId;
}
