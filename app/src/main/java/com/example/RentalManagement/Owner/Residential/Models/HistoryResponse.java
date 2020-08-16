package com.example.RentalManagement.Owner.Residential.Models;

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

    public HistoryResponse.data[] getData() {
        return data;
    }

    data[] data;
    public class data{
        String PostBy;//userId
        Integer PropertyId;
        String ApartmentName;
        String BHK;
        String Sft;
        String Rent;
        String RoomNo;

        public String getAddress() {
            return Address;
        }

        String Address;
        String Image2;
        public String getPostBy() {
            return PostBy;
        }

        public Integer getPropertyId() {
            return PropertyId;
        }

        public String getApartmentName() {
            return ApartmentName;
        }

        public String getBHK() {
            return BHK;
        }

        public String getSft() {
            return Sft;
        }

        public String getRent() {
            return Rent;
        }

        public String getRoomNo() {
            return RoomNo;
        }


        public String getImage2() {
            return Image2;
        }


    }

}
