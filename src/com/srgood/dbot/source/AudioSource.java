package com.srgood.dbot.source;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.io.FileNotFoundException;



public interface AudioSource
{
    String getSource();
    AudioInfo getInfo();
    AudioStream asStream();
    File asFile(String path, boolean deleteIfExists) throws FileAlreadyExistsException, FileNotFoundException;
}
