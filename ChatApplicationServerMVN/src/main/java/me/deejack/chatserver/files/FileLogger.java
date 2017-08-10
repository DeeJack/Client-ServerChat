package me.deejack.chatserver.files;

import me.deejack.chatserver.utils.Date;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author DeeJack
 */
public class FileLogger {
    private String path = System.getProperty("user.dir");
    private static FileLogger instance;

    private FileLogger() { }

    public static FileLogger getLogger() {
        if(instance == null)
            instance = new FileLogger();
        return instance;
    }

    public void log(String filename, String text) {
        File log = new File(path + "\\" + filename + ".txt");
        write(log, text);
    }

    private void write(File f, String s) {
        try {
            if(!f.exists())
                f.createNewFile();
            Files.write(Paths.get(f.getPath()), (new Date().toString() + s + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
