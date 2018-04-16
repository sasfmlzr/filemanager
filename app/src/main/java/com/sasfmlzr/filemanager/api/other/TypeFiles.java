package com.sasfmlzr.filemanager.api.other;

import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

public class TypeFiles {
    private static final HashMap<String, String> FILE_TYPES = new HashMap<>();
    static{
        /*
         * ================= FILE TYPES ====================
         */
        FILE_TYPES.put("asm", "text/x-asm");
        FILE_TYPES.put("def", "text/plain");
        FILE_TYPES.put("in", "text/plain");
        FILE_TYPES.put("rc", "text/plain");
        FILE_TYPES.put("list", "text/plain");
        FILE_TYPES.put("log", "text/plain");
        FILE_TYPES.put("pl", "text/plain");
        FILE_TYPES.put("prop", "text/plain");
        FILE_TYPES.put("properties", "text/plain");
        FILE_TYPES.put("rc", "text/plain");
        FILE_TYPES.put("epub", "application/epub+zip");
        FILE_TYPES.put("ibooks", "application/x-ibooks+zip");
        FILE_TYPES.put("ifb", "text/calendar");
        FILE_TYPES.put("eml", "message/rfc822");
        FILE_TYPES.put("msg", "application/vnd.ms-outlook");
        FILE_TYPES.put("ace", "application/x-ace-compressed");
        FILE_TYPES.put("bz", "application/x-bzip");
        FILE_TYPES.put("bz2", "application/x-bzip2");
        FILE_TYPES.put("cab", "application/vnd.ms-cab-compressed");
        FILE_TYPES.put("gz", "application/x-gzip");
        FILE_TYPES.put("lrf", "application/octet-stream");
        FILE_TYPES.put("jar", "application/java-archive");
        FILE_TYPES.put("xz", "application/x-xz");
        FILE_TYPES.put("Z", "application/x-compress");
        FILE_TYPES.put("bat", "application/x-msdownload");
        FILE_TYPES.put("ksh", "text/plain");
        FILE_TYPES.put("sh", "application/x-sh");
        FILE_TYPES.put("db", "application/octet-stream");
        FILE_TYPES.put("db3", "application/octet-stream");
        FILE_TYPES.put("otf", "x-font-otf");
        FILE_TYPES.put("ttf", "x-font-ttf");
        FILE_TYPES.put("psf", "x-font-linux-psf");
        FILE_TYPES.put("cgm", "image/cgm");
        FILE_TYPES.put("btif", "image/prs.btif");
        FILE_TYPES.put("dwg", "image/vnd.dwg");
        FILE_TYPES.put("dxf", "image/vnd.dxf");
        FILE_TYPES.put("fbs", "image/vnd.fastbidsheet");
        FILE_TYPES.put("fpx", "image/vnd.fpx");
        FILE_TYPES.put("fst", "image/vnd.fst");
        FILE_TYPES.put("mdi", "image/vnd.ms-mdi");
        FILE_TYPES.put("npx", "image/vnd.net-fpx");
        FILE_TYPES.put("xif", "image/vnd.xiff");
        FILE_TYPES.put("pct", "image/x-pict");
        FILE_TYPES.put("pic", "image/x-pict");
        FILE_TYPES.put("adp", "audio/adpcm");
        FILE_TYPES.put("au", "audio/basic");
        FILE_TYPES.put("snd", "audio/basic");
        FILE_TYPES.put("m2a", "audio/mpeg");
        FILE_TYPES.put("m3a", "audio/mpeg");
        FILE_TYPES.put("oga", "audio/ogg");
        FILE_TYPES.put("spx", "audio/ogg");
        FILE_TYPES.put("aac", "audio/x-aac");
        FILE_TYPES.put("mka", "audio/x-matroska");
        FILE_TYPES.put("jpgv", "video/jpeg");
        FILE_TYPES.put("jpgm", "video/jpm");
        FILE_TYPES.put("jpm", "video/jpm");
        FILE_TYPES.put("mj2", "video/mj2");
        FILE_TYPES.put("mjp2", "video/mj2");
        FILE_TYPES.put("mpa", "video/mpeg");
        FILE_TYPES.put("ogv", "video/ogg");
        FILE_TYPES.put("flv", "video/x-flv");
        FILE_TYPES.put("mkv", "video/x-matroska");
    }

    public static String getFileType(File file) {
        if (file.isDirectory()) {
            return null;
        }
        String type = null;
        final String extension = getExtension(file.getName());
        if (extension != null && !extension.isEmpty()) {
            final String extensionLowerCase = extension.toLowerCase(Locale.getDefault());
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extensionLowerCase);
            if (type == null) {
                type = FILE_TYPES.get(extensionLowerCase);
            }
        }
        if (type == null)
            type = "*/*";
        return type;
    }

    private static String getExtension(String name) {
        String ext;
        if (name.lastIndexOf(".") == -1) {
            ext = "";
        } else {
            int index = name.lastIndexOf(".");
            ext = name.substring(index + 1, name.length());
        }
        return ext;
    }
}
