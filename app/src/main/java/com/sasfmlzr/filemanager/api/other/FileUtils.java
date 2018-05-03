package com.sasfmlzr.filemanager.api.other;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public class FileUtils {
    private static final long ONE_KB = 1024;
    private static final BigInteger KB_BI = BigInteger.valueOf(ONE_KB);
    private static final BigInteger MB_BI = KB_BI.multiply(KB_BI);
    private static final BigInteger GB_BI = KB_BI.multiply(MB_BI);
    private static final BigInteger TB_BI = KB_BI.multiply(GB_BI);

    public static String formatCalculatedSize(long ls) {
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

    public static long getDirectorySize(File directory) {
        final File[] files = directory.listFiles();
        long size = 0;
        if (files == null) {
            return 0L;
        }

        for (final File file : files) {
            try {
                if (!isSymlink(file)) {
                    size += sizeOf(file);
                    if (size < 0) {
                        break;
                    }
                }
            } catch (IOException ioe) {
                // ignore exception when asking for symlink
            }
        }
        return size;
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
            return getDirectorySize(file);
        } else {
            return file.length();
        }
    }
}
