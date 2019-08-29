package com.wootube.ioi.web.argument;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Redirection {
    private String redirectUrl;

    Redirection(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
