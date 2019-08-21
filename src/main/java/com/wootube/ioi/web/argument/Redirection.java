package com.wootube.ioi.web.argument;

import lombok.Getter;

@Getter
public class Redirection {
    private String redirectUrl;

    public Redirection() {
    }

    public Redirection(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
