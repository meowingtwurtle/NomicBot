
package com.derek.source;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

public interface AudioSource
{
    String getSource();
    AudioInfo getInfo();
    AudioStream asStream();
    File asFile(String path, boolean deleteOnExists) throws FileAlreadyExistsException, FileNotFoundException;
}
