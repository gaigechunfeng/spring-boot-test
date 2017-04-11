package com.wk.boot.util;

import com.wk.boot.model.User;

/**
 * Created by gaige on 2017/4/7.
 */
public class Msg<T> {

    public static final Msg SUCCESS = new Msg(true, null, null);
    public static final Msg ERROR = new Msg(false, null, null);
    private boolean success;
    private String errmsg;
    private T obj;

    public Msg() {
    }

    public Msg(boolean b, String o, T o1) {
        success = b;
        errmsg = o;
        obj = o1;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public static <T> Msg success(T o) {

        return new Msg(true, null, o);
    }

    public static Msg error(Exception e) {
        return new Msg(false, e.getMessage(), null);
    }
}
