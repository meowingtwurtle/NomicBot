package com.srgood.dbot.utils;

import java.security.SecureRandom;
import java.math.BigInteger;

public final class SecureOverrideKeyGenerator {
  private static final SecureRandom random = new SecureRandom();

  public static String nextOverrideKey() {
    return new BigInteger(130, random).toString(32);
  }
}