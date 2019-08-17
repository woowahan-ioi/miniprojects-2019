package com.wootube.ioi.web.session;

import lombok.Getter;

@Getter
public class UserSession {
    private Long id;
    private String name;
    private String email;

    UserSession(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
