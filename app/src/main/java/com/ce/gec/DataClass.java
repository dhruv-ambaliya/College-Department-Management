package com.ce.gec;

import java.util.Date;

public class DataClass {

    private String dataTitle;
    private String dataDesc;
    private String dataImage;
    private String key;

    private  String fileType;

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public void setDataDesc(String dataDesc) {
        this.dataDesc = dataDesc;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public String getDataImage() {
        return dataImage;
    }

    private long timestamp;

    public DataClass(String dataTitle, String dataDesc, String dataImage, String fileType, long timestamp) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataImage = dataImage;
        this.fileType = fileType;
        this.timestamp = timestamp;
    }

    public DataClass(String dataTitle, String dataDesc, String dataImage, String key, String fileType, long timestamp) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataImage = dataImage;
        this.key = key;
        this.fileType = fileType;
        this.timestamp = timestamp;
    }

    public DataClass(){

    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


}
