package com.testbuddy.TestBuddyTelemetryServer.security;


import org.apache.commons.codec.digest.*;

public class Md5Hasher implements Hasher {

    @Override
    public String hash(String message) {
        return DigestUtils.md5Hex(message);
    }
}
