package com.finalproject.dean.finalprojectapplication;

/**
 * Created by Dean on 01/04/2015.
 */
public class Users {
    int id;
    String userName;
    String passWord;

    // constructors
    public Users() {
    }

    public Users(String username,String password) {
        this.userName = username;
        this.passWord = password;
    }

    public Users(int id, String username, String password) {
        this.id = id;
        this.userName = username;
        this.passWord = password;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public void setPassWord(String password) {
        this.passWord = password;
    }

    // getters
    public long getId() {
        return this.id;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassWord() {
        return this.passWord;
    }
}
