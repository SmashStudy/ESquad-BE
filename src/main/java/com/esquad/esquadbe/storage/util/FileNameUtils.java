package com.esquad.esquadbe.storage.util;

public class FileNameUtils {

    private FileNameUtils() {
    }

    public static final String FILE_EXTENSION_SEPARATOR = ".";

    public static String getExtension(String originalFileName) {
        int index = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        return (index > 0) ? originalFileName.substring(index) : "";
    }

    public static String getBaseFileName(String originalFileName) {
        int index = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        return (index > 0) ? originalFileName.substring(0, index) : originalFileName;
    }

    public static String buildUniqueFileName(String baseFileName, String extension) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return baseFileName + "_" + timestamp + extension;
    }
}
