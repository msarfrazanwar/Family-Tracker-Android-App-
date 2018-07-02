package com.example.inamul.projectclg;

/**
 * Created by Inamul on 8/18/2017.
 */

public class User {
    private String user_Id;
    private String userName;
    private String userPhone;
    private String userGroup;


    public User(){

    }






    public User(String userName,String userGroup){
        this.userName = userName;

        this.userGroup = userGroup;

    }

    public   User( String userName,String userPhone,String userGroup){
        this.userName = userName;
        this.userPhone = userPhone;
        this.userGroup = userGroup;

    }

    public String getUserName(){
        return userName;

    }

    public String getUserGroup(){
        return userGroup;

    }

    public String getUserPhone()
    {
        return userPhone;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public  void  setUserPhone(String userPhone){
        this.userPhone = userPhone;
    }

    public  void  setUserGroup(String userGroup){
        this.userGroup = userGroup;
    }
}
