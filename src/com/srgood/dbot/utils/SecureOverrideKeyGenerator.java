package com.srgood.dbot.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class SecureOverrideKeyGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String nextOverrideKey() {
        return new BigInteger(130, random).toString(32);
    }
}