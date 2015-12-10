package com.ydwj.bean;

/**
 * Created by MangoCandy on 2015/10/16.
 */
public class Userinfo {
    public static  String USER_NAME_CODE="users_name";
    public static  String USER_IMG_CODE="avatar_url";
    public static String USER_LOGINNAME_CODE="login_name";
    public static  String USER_IDCARD_CODE="card_id";
    public static String USER_EMAIL_CODE="users_email";
    public static String USER_ID_CODE="id";
    public static String USER_is_homeowner_CODE="is_homeowner";
    public static String USER_ADDRESS_CODE="users_address";
    public static String USER_TEL1_CODE="users_tel1";
    public static String USER_TEL2_CODE="users_tel2";
    public static String USER_PWD="login_pwd";

    private String USER_NAME;
    private String USER_IMG;
    private String login_pwd;
    private String login_name;
    private String card_id;
    private String user_address;
    private String ID;
    private String Email;
    private String is_homeowner;
    private String users_tel1;
    private String users_tel2;

    public String getUsers_tel1() {
        return users_tel1;
    }

    public void setUsers_tel1(String users_tel1) {
        this.users_tel1 = users_tel1;
    }

    public String getUsers_tel2() {
        return users_tel2;
    }

    public void setUsers_tel2(String users_tel2) {
        this.users_tel2 = users_tel2;
    }

    public String getIs_homeowner() {
        return is_homeowner;
    }

    public void setIs_homeowner(String is_homeowner) {
        this.is_homeowner = is_homeowner;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getUSER_IMG() {
        return USER_IMG;
    }

    public void setUSER_IMG(String USER_IMG) {
        this.USER_IMG = USER_IMG;
    }

    public String getLogin_pwd() {
        return login_pwd;
    }

    public void setLogin_pwd(String login_pwd) {
        this.login_pwd = login_pwd;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }
}
