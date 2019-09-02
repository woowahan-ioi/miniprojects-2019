package com.wootube.ioi.web.session;

import com.wootube.ioi.domain.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserSessionManager extends SessionManagerGenerator {
    private final ModelMapper modelMapper;

    public UserSessionManager(HttpServletRequest request, ModelMapper modelMapper) {
        super(request);
        this.modelMapper = modelMapper;
    }

    public UserSession getUserSession() {
        return (UserSession) super.get("user");
    }

    public void setUserSession(User user) {
        super.set("user", modelMapper.map(user, UserSession.class));
    }

    public void removeUserSession() {
        super.remove("user");
    }
}
