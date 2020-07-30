package com.example.RentalManagement.Owner.Model;

public class AddPropertyResponse {
    String enteredLocation;
    String latitude;
    String longitude;
    String apartment;
    String bhk;
    String extent;
    String rent;
    String floors;
    String floorNo;
    String tenants;
    String food;
    String specialFeatures;
    String water;
    String parking;
    String lift;
    String contactTime;
    String address;

    String image1, imageName1, image2, imageName2, image3, imageName3, propertyId;

    public AddPropertyResponse(String image1, String imageName1,
                               String image2, String imageName2, String image3, String imageName3,
                               String propertyId) {
        this.image1 = image1;
        this.imageName1 = imageName1;
        this.image2 = image2;
        this.imageName2 = imageName2;
        this.image3 = image3;
        this.imageName3 = imageName3;
        this.propertyId = propertyId;
    }


    public String getStatus() {
        return Status;
    }

    String Status;

    public AddPropertyResponse(String enteredLocation, String latitude, String longitude,
                               String apartment, String bhk, String extent, String rent,
                               String floors, String floorNo, String tenants, String food,
                               String specialFeatures, String water, String parking, String lift,
                               String contactTime, String address) {
        this.enteredLocation = enteredLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.apartment = apartment;
        this.bhk = bhk;
        this.extent = extent;
        this.rent = rent;
        this.floors = floors;
        this.floorNo = floorNo;
        this.tenants = tenants;
        this.food = food;
        this.specialFeatures = specialFeatures;
        this.water = water;
        this.parking = parking;
        this.lift = lift;
        this.contactTime = contactTime;
        this.address = address;
    }
}
