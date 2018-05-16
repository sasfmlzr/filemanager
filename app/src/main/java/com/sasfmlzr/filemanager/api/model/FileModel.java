package com.sasfmlzr.filemanager.api.model;
import java.io.File;

public class FileModel {
    private File file;
    private Long sizeDirectory;

    public FileModel(File file, Long size) {
        this.file = file;
        this.sizeDirectory = size;
    }

    public FileModel(File file) {
        this.file = file;
        this.sizeDirectory = 0L;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getSizeDirectory() {
        return sizeDirectory;
    }

    public void setSizeDirectory(Long sizeDirectory) {
        this.sizeDirectory = sizeDirectory;
    }
}
