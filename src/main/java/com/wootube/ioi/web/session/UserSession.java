package com.wootube.ioi.web.session;

import com.wootube.ioi.domain.model.User;

import lombok.Getter;

@Getter
public class UserSession {
    private Long id;
    private String name;
    private String email;

    public UserSession(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static UserSession of(User user) {
        return new UserSession(user.getId(), user.getName(), user.getEmail());
    }
}
