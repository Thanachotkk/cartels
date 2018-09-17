package com.internship.nilecon.cartels.API

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface AuthenticationsInterface{

    @POST("api/Auth/SentOtpSmsForSignUp")
    fun sentOtpSmsForSignUp(@Body userForSentOtpSmsForSignUpDTO: UserForSentOtpSmsForSignUpDTO) : Call<Void>

    @POST("api/Auth/VerifyOtp")
    fun verifyOtpForSignUp(@Body userForVerifyOtpDTO: UserForVerifyOtpDTO) : Call<Void>

    @POST("api/Auth/VerifyOtp")
    fun verifyOtpForForgotPassword(@Body userForVerifyOtpDTO: UserForVerifyOtpDTO) : Call<Token>

    @POST(" /api/Auth/IsUserExistsBySocial")
    fun isUserExistsBySocial (@Body userForIsUserExistsBySocialDTO : UserForIsUserExistsBySocialDTO ): Call<Void>

    @POST("api/Auth/SignUp")
    fun signUp(@Body userForSignUpDTO: UserForSignUpDTO) : Call<Token>

    @POST("api/Auth/SignInForMobileNumber")
    fun signInForMobileNumber(@Body userForSignInForMobileNumberDTO : UserForSignInForMobileNumberDTO) : Call<Token>

    @POST("api/Auth/ForgotPassword")
    fun forgotPasswor(@Body userForForgotPasswordDTO: UserForForgotPasswordDTO) : Call<Void>

    @POST("api/Auth/SignInForSocial")
    fun signInForSocial(@Body userForSignInForSocialDTO : UserForSignInSocialDTO) : Call<Token>

    @PATCH("api/Auth/ResetPassword")
    fun resetPassword (@Header("Authorization") token : String,
                       @Body userForResetPasswordDTO: UserForResetPasswordDTO) : Call<Void>
}

interface UsersInterface{
    @Multipart
    @POST("api/Users/AddOrReplacePhoto")
    fun addOrReplacePhoto(@Header("Authorization") token : String,
                          @Part image : MultipartBody.Part) : Call<Void>
}

interface ParkingsInterface{
    @POST("api/Parkings/GetParkingPointByLatLng")
    fun getParkingPointByLatLng(@Header("Authorization") token: String
                                ,@Body parkingForGetParkingPointByLatLngDTO:ParkingForGetParkingPointByLatLngDTO) : Call<List<ParkingPoint>>

    @POST("api/Parkings/GetParkingPoints")
    fun getParkingPoints(@Header("Authorization") token: String,
                         @Body ParkingForGetParkingPointsDTO: ParkingForGetParkingPointsDTO) : Call<List<ParkingPoint>>


    @POST("api/Parkings/GetParkingDetail")
    fun getParkingDetail(@Header("Authorization") token: String
                                ,@Body parkingForGetParkingDetailDTO: ParkingForGetParkingDetailDTO) : Call<ParkingDetail>

}