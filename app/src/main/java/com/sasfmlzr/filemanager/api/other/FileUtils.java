package com.sasfmlzr.filemanager.api.other;

import com.sasfmlzr.filemanager.api.model.FileModel;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final long ONE_KB = 1024;
    private static final BigInteger KB_BI = BigInteger.valueOf(ONE_KB);
    private static final BigInteger MB_BI = KB_BI.multiply(KB_BI);
    private static final BigInteger GB_BI = KB_BI.multiply(MB_BI);
    private static final BigInteger TB_BI = KB_BI.multiply(GB_BI);

    public static String formatCalculatedSize(Long ls) {
        BigInteger size = BigInteger.valueOf(ls);
        String displaySize;

        if (size.divide(TB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(TB_BI)) + " TB";
        } else if (size.divide(GB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(GB_BI)) + " GB";
        } else if (size.divide(MB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(MB_BI)) + " MB";
        } else if (size.divide(KB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(KB_BI)) + " KB";
        } else {
            displaySize = String.valueOf(size) + " bytes";
        }
        return displaySize;
    }

    public static List<FileModel> getDirectorySize(File directory) {
        final File[] files = directory.listFiles();
        List<FileModel> fileModels = new ArrayList<>();
        long size = 0;
        if (files == null) {
            return null;
        }
        for (final File file : files) {
            try {
                if (!isSymlink(file)) {
                    long sizeInnerFile = sizeOf(file);
                    fileModels.add(new FileModel(file, sizeInnerFile));
                    size += sizeInnerFile;
                    if (size < 0) {
                        break;
                    }
                }
            } catch (IOException ioe) {
                // ignore exception when asking for symlink
            }
        }
        fileModels.add(new FileModel(directory, size));
        return fileModels;
    }

    public static List<FileModel> getOnlyDirectory(List<FileModel> fileModels) {
        List<FileModel> fileModelResult = new ArrayList<>();
        for (FileModel fm : fileModels) {
            if (fm.getFile().isDirectory()) {
                fileModelResult.add(fm);
            }
        }
        return fileModelResult;
    }

    private static boolean isSymlink(File file) throws IOException {
        File fileInCanonicalDir;

        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }
        return !fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile());
    }

    private static long sizeOf(File file) {
        if (file.isDirectory()) {
            List<FileModel> list = getDirectorySize(file);
            assert list != null;
            return list.get(list.size() - 1).getSizeDirectory();
        } else {
            return file.length();
        }
    }
}
