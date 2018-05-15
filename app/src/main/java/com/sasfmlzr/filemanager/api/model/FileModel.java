package com.sasfmlzr.filemanager.api.model;
import java.io.File;

public class FileModel {
    private File file;
    private String sizeDirectory;

    public FileModel(File file, String size) {
        this.file = file;
        this.sizeDirectory = size;
    }

    public FileModel(File file) {
        this.file = file;
        this.sizeDirectory = null;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getSizeDirectory() {
        return sizeDirectory;
    }

    public void setSizeDirectory(String sizeDirectory) {
        this.sizeDirectory = sizeDirectory;
    }
}
