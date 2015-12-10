package com.ydwj.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;

/**
 * Created by Administrator on 2015/10/30.
 */
public class Contacts {
    @Column(column = "CONTACT_NAME")
    String CONTACT_NAME;
    @Column(column = "CONTACT_NUM")
    String CONTACT_NUM;
    @Id(column = "ID")
    int ID;
    @Column(column = "CID")
    String CID;
    @Column(column = "IS_UPDATE")
    String isUpdate;


    public String getIsUpdate() {
        return isUpdate;
    }

    public String isUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBEIZHU() {
        return BEIZHU;
    }

    public void setBEIZHU(String BEIZHU) {
        this.BEIZHU = BEIZHU;
    }

    @Column(column = "BEIZHU")
    String BEIZHU;

    public String getCONTACT_NAME() {
        return CONTACT_NAME;
    }

    public void setCONTACT_NAME(String CONTACT_NAME) {
        this.CONTACT_NAME = CONTACT_NAME;
    }

    public String getCONTACT_NUM() {
        return CONTACT_NUM;
    }

    public void setCONTACT_NUM(String CONTACT_NUM) {
        this.CONTACT_NUM = CONTACT_NUM;
    }
}
