package com.example.RentalManagement.Services;


import com.example.RentalManagement.Model.ChangePasswordResponse;
import com.example.RentalManagement.Model.ForgotPasswordResponse;
import com.example.RentalManagement.Model.LoginResponse;
import com.example.RentalManagement.Model.RegistrationResponse;
import com.example.RentalManagement.Owner.Model.AddPropertyResponse;
import com.example.RentalManagement.Owner.Model.HistoryResponse;
import com.example.RentalManagement.Tenant.Model.SearchPropertyResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    /*get data to edit propertydetails by owner*/
    @Headers("Content-Type:application/json")
    @GET("Property/{id}")
    Call<AddPropertyResponse> getHistory(
            @Path("id") int id
    );
    /*Call<List<HistoryResponse>> getHistory(
            @Body HistoryResponse historyResponse
    );*/

    /*search property for tenants*/
    @Headers("Content-Type:application/json")
    Call<SearchPropertyResponse> getSearchPropertyResults(
            @Body SearchPropertyResponse searchPropertyResponse
    );


}
