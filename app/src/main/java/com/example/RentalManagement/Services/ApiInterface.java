package com.example.RentalManagement.Services;


import com.example.RentalManagement.Model.ChangePasswordResponse;
import com.example.RentalManagement.Model.ForgotPasswordResponse;
import com.example.RentalManagement.Model.LoginResponse;
import com.example.RentalManagement.Model.RegistrationResponse;
import com.example.RentalManagement.Owner.Residential.Models.AddPropertyResponse;
import com.example.RentalManagement.Owner.Residential.Models.HistoryResponse;
import com.example.RentalManagement.Tenant.Models.SearchPropertyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    /*login*/
    @Headers("Content-Type: application/json")
    @POST("UserLogin/Login")
    Call<LoginResponse> getLoginStatus(
            @Body LoginResponse loginResponse
    );

    /*registration send otp*/
    @Headers("Content-Type:application/json")
    @POST("Users/GetOTP")
    Call<RegistrationResponse> SendOtpStatus(
            @Body RegistrationResponse registrationResponse);

    /*registration inserting details after otp verification*/
    @Headers("Content-Type:application/json")
    @POST("Users/UserRegistration")
    Call<RegistrationResponse> ValidateOtp(
            @Body RegistrationResponse registrationResponse
    );


    /*submitting property details and images*/
    @Headers("Content-Type:application/json")
    @POST("Property/PropertyRegistration")
    Call<AddPropertyResponse> uploadPropertyDetails(
            @Body AddPropertyResponse addPropertyResponse
    );

    /*forgot password*/
    @Headers("Content-Type:application/json")
    @POST("Users/ForgetPasswordOTP")
    Call<ForgotPasswordResponse> getPasswordSendOtpStatus(
            @Body ForgotPasswordResponse forgotPasswordResponse
    );

    /*change password*/
    @Headers("Content-Type:application/json")
    @POST("Users/ChangePassword")
    Call<ChangePasswordResponse> getChangePasswordStatus(
            @Body ChangePasswordResponse changePasswordResponse
    );

    /*get history of owner*/
    @Headers("Content-Type:application/json")
    @GET(" Property/GetAllPropertiesByUserid/{id}")
    Call<HistoryResponse> getHistory(
            @Path("id") int id /*here id means user id*/
    );

    /*get proprertyId data for updating propertydetails by owner*/
    @Headers("Content-Type:application/json")
    @GET("Property/{id}")
    Call<AddPropertyResponse> getPropertyIdData(
            @Path("id") int id
    );

    /*update property details*/
    @Headers("Content-Type:application/json")
    @POST("Property/PropertyUpdate")
    Call<AddPropertyResponse> updatePropertyDetails(
            @Body AddPropertyResponse addPropertyResponse
    );


    /*search property for tenants*/
    @Headers("Content-Type:application/json")
    Call<SearchPropertyResponse> getSearchPropertyResults(
            @Body SearchPropertyResponse searchPropertyResponse
    );

    /*uploading commercial property detials*/
    @Headers("Content-Type:application/json")
    Call<AddPropertyResponse> uploadCommercialPropertyDetails(
            @Body AddPropertyResponse addPropertyResponse
    );
}
