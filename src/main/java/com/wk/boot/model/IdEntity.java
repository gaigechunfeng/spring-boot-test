package com.wk.boot.model;

/**
 * Created by 005689 on 2017/4/12.
 */
public abstract class IdEntity {

    private long id;
    private String uui;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUui() {
        return uui;
    }

    public void setUui(String uui) {
        this.uui = uui;
    }
}
