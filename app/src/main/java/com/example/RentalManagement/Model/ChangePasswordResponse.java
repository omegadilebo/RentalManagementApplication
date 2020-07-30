package com.example.RentalManagement.Model;

public class ChangePasswordResponse {
    String MobileNo;
    String Password;

    public String getStatus() {
        return Status;
    }

    String Status;

    public ChangePasswordResponse(String mobileNo, String password) {
        this.MobileNo = mobileNo;
        this.Password = password;
    }
}
