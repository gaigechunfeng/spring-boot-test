package com.wk.boot.model;

/**
 * Created by 005689 on 2017/4/12.
 */
public abstract class BaseEntity extends IdEntity {

    //whether deleted,是否已删除
    private boolean wde;
    //whether offline,是否离线版本
    private boolean wof;
    //whether synchronized to center,是否已同步到中心
    private boolean wsc;
    //online version,在线版本序号
    private long olv;
    //offline version,离线版本序号
    private long ofv;
    //site clock difference,中心时钟相对外站时钟的时间差，毫秒值
    private long scd;
    //site three code,数据所属外站应用唯一标识,通用数据表示为center
    private String stc;
    //操作类型,insert,update,delete
    private String act;
    //creation time,创建时间
    private String ct;
    //last modified time,最近一次操作时间
    private String lmt;
    //last offline modified time,最近一次离线操作时间
    private String omt;
    //last modified by,最近一次操作用户uuid,0表示无操作用户
    private String lmb;
    //log uuid,操作日志uuid,0表示无操作日志
    private String lu;
    //航班唯一标识
    private String fu;


    public boolean isWde() {
        return wde;
    }

    public void setWde(boolean wde) {
        this.wde = wde;
    }

    public boolean isWof() {
        return wof;
    }

    public void setWof(boolean wof) {
        this.wof = wof;
    }

    public boolean isWsc() {
        return wsc;
    }

    public void setWsc(boolean wsc) {
        this.wsc = wsc;
    }

    public long getOlv() {
        return olv;
    }

    public void setOlv(long olv) {
        this.olv = olv;
    }

    public long getOfv() {
        return ofv;
    }

    public void setOfv(long ofv) {
        this.ofv = ofv;
    }

    public long getScd() {
        return scd;
    }

    public void setScd(long scd) {
        this.scd = scd;
    }

    public String getStc() {
        return stc;
    }

    public void setStc(String stc) {
        this.stc = stc;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getLmt() {
        return lmt;
    }

    public void setLmt(String lmt) {
        this.lmt = lmt;
    }

    public String getOmt() {
        return omt;
    }

    public void setOmt(String omt) {
        this.omt = omt;
    }

    public String getLmb() {
        return lmb;
    }

    public void setLmb(String lmb) {
        this.lmb = lmb;
    }

    public String getLu() {
        return lu;
    }

    public void setLu(String lu) {
        this.lu = lu;
    }

    public String getFu() {
        return fu;
    }

    public void setFu(String fu) {
        this.fu = fu;
    }
}
