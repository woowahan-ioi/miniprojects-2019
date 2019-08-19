package com.wootube.ioi.web.session;

import javax.servlet.http.HttpServletRequest;

import com.wootube.ioi.domain.model.User;

import org.springframework.stereotype.Component;

@Component
public class UserSessionManager extends SessionManagerGenerator {

    public UserSessionManager(HttpServletRequest request) {
        super(request);
    }

    public UserSession getUserSession() {
        return (UserSession) super.get("user");
    }

    public void setUserSession(User user) {
        super.set("user", new UserSession(user.getId(), user.getName(), user.getEmail()));
    }

    public void removeUserSession() {
        super.remove("user");
    }
}
