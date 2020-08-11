package com.example.RentalManagement.Model;

public class LoginResponse {
    public String getMobileNo() {
        return MobileNo;
    }

    String MobileNo;
    String Password;

    public String getUserID() {
        return UserID;
    }

    String UserID;

    public String getStatus() {
        return Status;
    }

    String Status;

    public LoginResponse(String mobileNo, String password) {
        this.MobileNo = mobileNo;
        this.Password = password;
    }
}
