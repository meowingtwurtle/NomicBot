package com.srgood.dbot.source;

import org.json.JSONObject;

public class AudioInfo
{
    protected JSONObject jsonInfo;
    protected String title;
    protected String origin;
    protected String id;
    protected String encoding;
    protected String description;
    protected String extractor;
    protected String thumbnail;
    protected String error;
    protected boolean isLive;
    protected AudioTimestamp duration;

    public JSONObject getJsonInfo()
    {
        return jsonInfo;
    }

    public String getTitle()
    {
        return title;
    }

    public String getOrigin()
    {
        return origin;
    }

    public String getId()
    {
        return id;
    }

    public String getEncoding()
    {
        return encoding;
    }

    public String getDescription()
    {
        return description;
    }

    public String getExtractor()
    {
        return extractor;
    }

    public String getThumbnail()
    {
        return thumbnail;
    }

    public String getError()
    {
        return error;
    }

    public boolean isLive() {
        return isLive;
    }

    public AudioTimestamp getDuration()
    {
        return duration;
    }
}