package com.example.RentalManagement.Model;

public class ForgotPasswordResponse {
    String MobileNo;

    public String getStatus() {
        return Status;
    }

    String Status;

    public String getOTPNumber() {
        return OTPNumber;
    }

    String OTPNumber;
    String Password;

    public ForgotPasswordResponse(String mobileNo) {
        MobileNo = mobileNo;
    }


    public ForgotPasswordResponse(String mobileNo, String otp) {
        this.MobileNo = mobileNo;
        this.OTPNumber = otp;
    }

}
