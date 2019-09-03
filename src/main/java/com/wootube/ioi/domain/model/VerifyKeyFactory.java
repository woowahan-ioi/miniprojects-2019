package com.wootube.ioi.domain.model;

import java.util.Random;

public class VerifyKeyFactory {

    public static String createKey() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        return String.valueOf(random.nextInt());
    }
}
