package com.namtn.media.core.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserContext {
    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;

    public String getUserName() {
        return userName;
    }
}
