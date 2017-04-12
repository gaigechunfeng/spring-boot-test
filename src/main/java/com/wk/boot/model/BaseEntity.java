package com.wk.boot.model;

/**
 * Created by 005689 on 2017/4/12.
 */
public abstract class BaseEntity extends IdEntity {

    //whether deleted,�Ƿ���ɾ��
    private boolean wde;
    //whether offline,�Ƿ����߰汾
    private boolean wof;
    //whether synchronized to center,�Ƿ���ͬ��������
    private boolean wsc;
    //online version,���߰汾���
    private long olv;
    //offline version,���߰汾���
    private long ofv;
    //site clock difference,����ʱ�������վʱ�ӵ�ʱ������ֵ
    private long scd;
    //site three code,����������վӦ��Ψһ��ʶ,ͨ�����ݱ�ʾΪcenter
    private String stc;
    //��������,insert,update,delete
    private String act;
    //creation time,����ʱ��
    private String ct;
    //last modified time,���һ�β���ʱ��
    private String lmt;
    //last offline modified time,���һ�����߲���ʱ��
    private String omt;
    //last modified by,���һ�β����û�uuid,0��ʾ�޲����û�
    private String lmb;
    //log uuid,������־uuid,0��ʾ�޲�����־
    private String lu;
    //����Ψһ��ʶ
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
