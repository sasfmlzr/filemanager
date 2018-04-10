package com.sasfmlzr.filemanager.api.model;

public class FileModel {

    private String nameFile;
    private String typeFile;
    private String dateFile;

    public FileModel(String nameFile, String typeFile, String dateFile){
        this.nameFile = nameFile;
        this.typeFile = typeFile;
        this.dateFile = dateFile;
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
}
