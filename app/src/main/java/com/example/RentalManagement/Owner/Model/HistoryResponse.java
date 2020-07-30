package com.example.RentalManagement.Owner.Model;

public class HistoryResponse {
    String imagePath, MobileNo;

    public HistoryResponse(String mobileNo) {
        this.MobileNo = mobileNo;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    String address;
    String status;
}
