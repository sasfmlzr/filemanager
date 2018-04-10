package com.sasfmlzr.filemanager.api.model;

public class FileModel {

    private String nameFile;
    private String typeFile;
    private String dateFile;
    private int imageIconFile;
    public FileModel(String nameFile, String typeFile, String dateFile, int imageIconFile){
        this.nameFile = nameFile;
        this.typeFile = typeFile;
        this.dateFile = dateFile;
        this.imageIconFile = imageIconFile;
    }
    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getTypeFile() {
        return typeFile;
    }

    public void setTypeFile(String typeFile) {
        this.typeFile = typeFile;
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
}
