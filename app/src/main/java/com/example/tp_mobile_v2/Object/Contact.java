package com.example.tp_mobile_v2.Object;

/**
 * Created by LapTrinhMobile on 12/14/2015.
 */
public class Contact {
    String Id;
    String name;
    String phoneNumber;
    boolean isCheck;

    public Contact() {
        Id = "";
        name = "";
        phoneNumber = "";
        isCheck = false;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
