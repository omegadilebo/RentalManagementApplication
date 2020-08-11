package com.example.RentalManagement.Model;

public class ChangePasswordResponse {
    String MobileNo;
    String NewPassword;

    public String getStatus() {
        return Status;
    }

    String Status;

    public ChangePasswordResponse(String mobileNo, String password) {
        this.MobileNo = mobileNo;
        this.NewPassword = password;
    }
}
