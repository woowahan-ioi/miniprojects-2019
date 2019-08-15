package com.wootube.ioi.web.argument;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Redirection {
    private String redirectUrl;

    public Redirection(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
