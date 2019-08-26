package com.wootube.ioi.web.session;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSession {
    private Long id;
    private String name;
    private String email;

    public UserSession(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
