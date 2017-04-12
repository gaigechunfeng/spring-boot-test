package com.wk.boot.constant;

/**
 * Created by gaige on 2017/4/12.
 */
public enum Error {

    LOGIN_ERROR(100, "登录错误"),
    PASSWORD_ERROR(101, "密码错误"),
    UNKNOW_ACCOUNT(102, "不存在该用户"),
    NO_LOGIN(103, "尚未登录");

    final int errCode;
    final String errMsg;

    Error(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
