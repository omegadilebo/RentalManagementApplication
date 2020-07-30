package com.example.RentalManagement.Model;

public class RegistrationResponse {

    String UserName, UserAge, Occupation, Gender, MobileNo, Passwrd, IMENo,Otp;

    public RegistrationResponse(String otp) {
        this.Otp = otp;
    }

    public RegistrationResponse(String userName, String userAge, String occupation, String gender, String mobileNo, String passwrd, String IMENo) {
        this.UserName = userName;
        this.UserAge = userAge;
        this.Occupation = occupation;
        this.Gender = gender;
        this.MobileNo = mobileNo;
        this.Passwrd = passwrd;
        this.IMENo = IMENo;
    }

    public String getStatus() {
        return Status;
    }

    String Status;

}
