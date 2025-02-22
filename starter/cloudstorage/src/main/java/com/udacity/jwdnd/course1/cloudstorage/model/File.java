package com.udacity.jwdnd.course1.cloudstorage.model;

public class File {
    private Long fileId;
    private String filename;
    private String contenttype;
    private String filesize;
    private Integer userid;

    public File(Long fileId, String filename, String contentType,String filesize, Integer userid,byte[] filedata) {
        this.fileId = fileId;
        this.filename = filename;
        this.contenttype = contentType;
        this.filesize = filesize;
        this.userid = userid;
        this.filedata = filedata;
    }

    public File() {

    }

    public byte[] getFiledata() {
        return filedata;
    }

    public void setFiledata(byte[] filedata) {
        this.filedata = filedata;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    private byte[] filedata;
}
