package ru.mihaly4.vkmd.model;

public class Credential {
    private String remixSid = "";
    private Integer uid = 0;

    public String getRemixSid() {
        return remixSid;
    }

    public void setRemixSid(String remixSid) {
        this.remixSid = remixSid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
