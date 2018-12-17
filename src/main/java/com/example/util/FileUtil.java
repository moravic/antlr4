package com.example.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    /**
     * find files
     */
    public static List<File> getAllFiles(String dir) throws Exception {
        return getAllFiles(dir, null);
    }

    /**
     * find files
     */
    public static List<File> getAllFiles(String dir, String extension) {
        final List<File> ret = new ArrayList<File>();
        final File file = new File(dir);
        if (file.exists()) {
            final String[] list = file.list();
            if (null != list) {
                for (int i = 0; i < list.length; i++) {
                    final String fileName = dir + "/" + list[i];
                    final File f2 = new File(fileName);
                    if (!f2.isHidden()) {
                        if (f2.isDirectory()) {
                            ret.addAll(getAllFiles(fileName, extension));
                        } else {
                            if (null != extension) {
                                if (f2.getName().endsWith(extension)) {
                                    ret.add(f2);
                                }
                            } else {
                                ret.add(f2);
                            }
                        }
                    }
                }
            }
            return ret;
        } else {
            return null;
        }
    }

    public static List<String> getNonEmptyLines(File file, String encoding) throws IOException {
        final List<String> nonEmptyLines = new ArrayList<String>();
        BufferedReader br = null;
        InputStreamReader isr = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            try {
                isr = new InputStreamReader(fis, encoding);
                try {
                    br = new BufferedReader(isr);
                    String line = br.readLine();
                    while (line != null) {
                        if (!"".equals(line.trim())) {
                            nonEmptyLines.add(line);
                        }
                        line = br.readLine();
                    }
                    return nonEmptyLines;
                } finally {
                    if (br != null) {
                        br.close();
                    }
                }
            } finally {
                if (isr != null) {
                    isr.close();
                }
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
}

