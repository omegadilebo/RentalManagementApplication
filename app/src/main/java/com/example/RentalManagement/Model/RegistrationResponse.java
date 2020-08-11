package com.example.RentalManagement.Model;

import com.google.gson.annotations.SerializedName;

public class RegistrationResponse {

    String UserName;
    String UserAge;
    String Occupation;
    String Gender;
    String MobileNo;
    String Passwrd;
    String Roleid;
    String IMENo;
    String UserID;

    public String getUserID() {
        return UserID;
    }

    public String getOTPNumber() {
        return OTPNumber;
    }

    String OTPNumber;



    public RegistrationResponse(String userName, String userAge, String occupation, String gender, String mobileNo, String passwrd, String i,String IMENo) {
        this.UserName = userName;
        this.UserAge = userAge;
        this.Gender = gender;
        this.Occupation = occupation;
        this.MobileNo = mobileNo;
        this.Passwrd = passwrd;
        this.Roleid = i;
        this.IMENo = IMENo;
    }

    public RegistrationResponse(String getMobileNumber, String imei) {
        this.MobileNo = getMobileNumber;
        this.IMENo = imei;
    }


    public String getStatus() {
        return Status;
    }
    String Status;

}
