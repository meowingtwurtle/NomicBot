package com.srgood.dbot.source;

import java.io.BufferedInputStream;
import java.util.regex.Pattern;

public abstract class AudioStream extends BufferedInputStream
{
    public static final Pattern TIME_PATTERN = Pattern.compile("(?<=time=).*?(?= bitrate)");

    public AudioStream()
    {
        super(null);
    }

    public abstract AudioTimestamp getCurrentTimestamp();
}