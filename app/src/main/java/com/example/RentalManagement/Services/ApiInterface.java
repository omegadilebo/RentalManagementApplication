package com.example.RentalManagement.Services;


import com.example.RentalManagement.Model.ChangePasswordResponse;
import com.example.RentalManagement.Model.ForgotPasswordResponse;
import com.example.RentalManagement.Model.LoginResponse;
import com.example.RentalManagement.Model.RegistrationResponse;
import com.example.RentalManagement.Owner.Model.AddPropertyResponse;
import com.example.RentalManagement.Owner.Model.HistoryResponse;
import com.example.RentalManagement.Tenant.Model.SearchPropertyResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    /*login*/
    @Headers("Content-Type:application/json")
    Call<LoginResponse> getLoginStatus(
            @Body LoginResponse loginResponse
    );

    /*registration send otp*/
    @Headers("Content-Type:application/json")
    @POST("Users/UserRegistration")
    Call<RegistrationResponse> SendOtpStatus(
            @Body RegistrationResponse registrationResponse
    );

    /*registration validate otp*/
    @Headers("Content-Type:application/json")
    Call<RegistrationResponse> ValidateOtp(
            @Body RegistrationResponse registrationResponse
    );

    /*submitting property details*/
    @Headers("Content-Type:application/json")
    Call<AddPropertyResponse> getSubmitDetailsStatus(
            @Body AddPropertyResponse addPropertyResponse
    );

    /*submitting property images*/
    @Headers("Content-Type:application/json")
    Call<AddPropertyResponse> uploadImage(
            @Body AddPropertyResponse addPropertyResponse
    );

    /*forgot password*/
    @Headers("Content-Type:application/json")
    @POST("Users/GetOTP")
    Call<ForgotPasswordResponse> getPasswordSendOtpStatus(
            @Body ForgotPasswordResponse forgotPasswordResponse
    );

    @Headers("Content-Type:application/json")
    Call<ForgotPasswordResponse> getPasswordSubmitOtpStatus(
            @Body ForgotPasswordResponse forgotPasswordResponse
    );

    /*change password*/
    @Headers("Content-Type:application/json")
    Call<ChangePasswordResponse> getChangePasswordStatus(
            @Body ChangePasswordResponse changePasswordResponse
    );

    /*get history of owner*/
    @Headers("Content-Type:application/json")
    Call<List<HistoryResponse>> getHistory(
            @Body HistoryResponse historyResponse
    );



    /*search property for tenants*/
    @Headers("Content-Type:application/json")
    Call<SearchPropertyResponse> getSearchPropertyResults(
            @Body SearchPropertyResponse searchPropertyResponse
    );


}
