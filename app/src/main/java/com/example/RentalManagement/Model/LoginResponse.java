package com.example.RentalManagement.Model;

public class LoginResponse {
  String MobileNo;
    String Password;

    public String getStatus() {
        return Status;
    }

    String Status;

    public LoginResponse(String mobileNo, String password) {
        this.MobileNo = mobileNo;
        this.Password = password;
    }
}
