package com.wootube.ioi.web.session;

import com.wootube.ioi.domain.model.User;

import lombok.Getter;

@Getter
public class SessionUser {
    private Long id;
    private String name;
    private String email;

    public SessionUser(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static SessionUser of(User user) {
        return new SessionUser(user.getId(), user.getName(), user.getEmail());
    }
}
