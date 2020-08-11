package com.example.RentalManagement.Owner.Model;


public class AddPropertyResponse {
    String PostBy;//userId
    Integer PropertyId;
    String Location;//subLocality
    String Latitude;
    String Longitude;
    String Locality;
    String ApartmentType;
    String ApartmentName;
    String BHK;
    String Sft;
    String Rent;
    String RoomNo;//no of floors
    String FloorNo;
    String TenantType;
    String FoodType;
    String SplFeatures;
    String WaterSource;
    String ParkingType;
    String LiftAvailable;
    String ContactTime;
    String Address;

    String Image1;
    String Image2;
    String Image3;

    public String getUserID() {
        return UserID;
    }

    String UserID;

    public String getStatus() {
        return Status;
    }

    String Status;

    /*for interting and  updating property details
    * if propertyId == 0 (inserting) else (updating)
    * */
    public AddPropertyResponse(String userId, Integer propertyId, double latitude, double longitude,
                               String locality, String subLocality, String apartmentType,
                               String apartmentName, String bhk, String extent, String rent,
                               String floors, String floorNo, String tenantType, String foodType,
                               String specialFeatures, String water, String parking, String lift,
                               String contactTime, String address, String inside, String outside,
                               String specialArea) {
        this.PostBy = userId;
        this.PropertyId = propertyId;
        this.Latitude = String.valueOf(latitude);
        this.Longitude = String.valueOf(longitude);
        this.Locality = locality;
        this.Location = subLocality;
        this.ApartmentType = apartmentType;
        this.ApartmentName = apartmentName;
        this.BHK = bhk;
        this.Sft = extent;
        this.Rent = rent;
        this.RoomNo = floors;//no of floors
        this.FloorNo = floorNo;
        this.TenantType = tenantType;
        this.FoodType = foodType;
        this.SplFeatures = specialFeatures;
        this.WaterSource = water;
        this.ParkingType = parking;
        this.LiftAvailable = lift;
        this.ContactTime = contactTime;
        this.Address = address;
        this.Image1 = inside;
        this.Image2 = outside;
        this.Image3 = specialArea;
    }

    public String getPostBy() {
        return PostBy;
    }

    public String getLocation() {
        return Location;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getLocality() {
        return Locality;
    }

    public String getApartmentType() {
        return ApartmentType;
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

    public String getFloorNo() {
        return FloorNo;
    }

    public String getTenantType() {
        return TenantType;
    }

    public String getFoodType() {
        return FoodType;
    }

    public String getSplFeatures() {
        return SplFeatures;
    }

    public String getWaterSource() {
        return WaterSource;
    }

    public String getParkingType() {
        return ParkingType;
    }

    public String getLiftAvailable() {
        return LiftAvailable;
    }

    public String getContactTime() {
        return ContactTime;
    }

    public String getAddress() {
        return Address;
    }

    public String getImage1() {
        return Image1;
    }

    public String getImage2() {
        return Image2;
    }

    public String getImage3() {
        return Image3;
    }
}
