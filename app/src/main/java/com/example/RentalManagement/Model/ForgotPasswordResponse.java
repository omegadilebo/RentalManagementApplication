package com.example.RentalManagement.Model;

public class ForgotPasswordResponse {
    String MobileNo;
    String Status;
    String OTPNumber;
    String IMENo;

    public ForgotPasswordResponse(String mobileNo, String imei) {
        this.MobileNo = mobileNo;
        this.IMENo = imei;
    }


    public String getStatus() {
        return Status;
    }
    public String getOTPNumber() {
        return OTPNumber;
    }


}
