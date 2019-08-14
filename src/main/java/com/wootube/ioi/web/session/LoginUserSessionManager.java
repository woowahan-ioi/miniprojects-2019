package com.wootube.ioi.web.session;

import com.wootube.ioi.domain.model.User;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginUserSessionManager extends SessionManagerGenerator {

    public LoginUserSessionManager(HttpServletRequest request) {
        super(request);
    }

    public SessionUser getUser() {
        return (SessionUser) super.get("user");
    }

    public void setUser(User user) {
        super.set("user", SessionUser.of(user));
    }

    public void removeUser() {
        super.remove("user");
    }
}
