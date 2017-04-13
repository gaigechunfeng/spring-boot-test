package com.wk.boot.model;

import com.wk.boot.annotation.Entity;

/**
 * Created by gaige on 2017/4/7.
 */
//@Entity
//@Table(name = "user")
@Entity(tableName = "dcs_user")
public class User extends BaseEntity {

    //    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private String wid;
    private String pwd;
    private int pms;
    private int et;
    private String nm;
    private String mb;
    private String ap;
    private String llt = "";
    private String lli = "";
    private String pet;//password expire time 密码过期时间

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getPms() {
        return pms;
    }

    public void setPms(int pms) {
        this.pms = pms;
    }

    public int getEt() {
        return et;
    }

    public void setEt(int et) {
        this.et = et;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getMb() {
        return mb;
    }

    public void setMb(String mb) {
        this.mb = mb;
    }

    public String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public String getLlt() {
        return llt;
    }

    public void setLlt(String llt) {
        this.llt = llt;
    }

    public String getLli() {
        return lli;
    }

    public void setLli(String lli) {
        this.lli = lli;
    }

    public String getPet() {
        return pet;
    }

    public void setPet(String pet) {
        this.pet = pet;
    }
}
