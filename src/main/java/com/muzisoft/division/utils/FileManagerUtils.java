package com.muzisoft.division.utils;

import com.muzisoft.division.domain.file.EFileManager;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileManagerUtils {

    public static void delete(EFileManager fileManager) {
        File file = file(fileManager);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void delete(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean equals(EFileManager fileManager1, EFileManager fileManager2) {

        if(ObjectUtils.isEmpty(fileManager1) || ObjectUtils.isEmpty(fileManager2)) return false;

        FileInputStream in1;
        FileInputStream in2;

        try {
            in1 = new FileInputStream(file(fileManager1));
            in2 = new FileInputStream(file(fileManager2));

            boolean ret = IOUtils.contentEquals(in1, in2);

            in1.close();
            in2.close();

            return ret;

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
    public static File file(EFileManager fileManager) {
        return new File(new File("").getAbsolutePath() + "/files" + fileManager.getDirectoryManager().getDirPath() + "/" + fileManager.getFileName());
    }
}
