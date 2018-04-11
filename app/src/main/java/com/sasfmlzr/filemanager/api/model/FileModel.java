package com.sasfmlzr.filemanager.api.model;

public class FileModel {

    private String nameFile;
    private String dateFile;
    private String pathFile;
    private int imageIconFile;
    public FileModel(String nameFile, String dateFile, String pathFile, int imageIconFile){
        this.nameFile = nameFile;
        this.dateFile = dateFile;
        this.imageIconFile = imageIconFile;
        this.pathFile = pathFile;
    }
    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getDateFile() {
        return dateFile;
    }

    public void setDateFile(String dateFile) {
        this.dateFile = dateFile;
    }

    public int getImageIconFile() {
        return imageIconFile;
    }

    public void setImageIconFile(int imageIconFile) {
        this.imageIconFile = imageIconFile;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }
}
