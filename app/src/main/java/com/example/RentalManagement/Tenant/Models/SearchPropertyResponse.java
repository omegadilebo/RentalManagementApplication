package com.example.RentalManagement.Tenant.Models;

public class SearchPropertyResponse {
String category;
    String propertyType;
    String locality;
    String subLocality;
    String apartmentType;
    String bhk;
    String rent;
    String extent;

    public String getStatus() {
        return Status;
    }

    String Status;

    public SearchPropertyResponse(String category, String propertyType, String locality,
                                  String subLocality, String apartmentType, String bhk,
                                  String rent, String extent) {
        this.category = category;
        this.propertyType = propertyType;
        this.locality = locality;
        this.subLocality = subLocality;
        this.apartmentType = apartmentType;
        this.bhk = bhk;
        this.rent = rent;
        this.extent = extent;
    }
}
