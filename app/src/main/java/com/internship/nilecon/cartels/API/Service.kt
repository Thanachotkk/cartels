package com.internship.nilecon.cartels.API

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthenticationsInterface{

    @POST("api/Auth/SentOtpSmsForSignUp")
    fun sentOtpSmsForSignUp(@Body userForSentOtpSmsForSignUpDTO: UserForSentOtpSmsForSignUpDTO) : Call<Void>

    @POST("api/Auth/VerifyOtp")
    fun verifyOtpForSignUp(@Body userForVerifyOtpDTO: UserForVerifyOtpDTO) : Call<Void>

    @POST("api/Auth/VerifyOtp")
    fun verifyOtpForForgotPassword(@Body userForVerifyOtpDTO: UserForVerifyOtpDTO) : Call<Token>

    @POST("api/Auth/SignUp")
    fun signUp(@Body userForSignUpDTO: UserForSignUpDTO) : Call<Token>

    @POST("api/Auth/SignInForMobileNumber")
    fun signInForMobileNumber(@Body userForSignInForMobileNumberDTO : UserForSignInForMobileNumberDTO) : Call<Token>

    @POST("api/Auth/ForgotPassword")
    fun forgotPasswor(@Body userForForgotPasswordDTO: UserForForgotPasswordDTO) : Call<Void>

    @POST("api/Auth/SignInForSocial")
    fun signInForSocial(@Body userForSignInForSocialDTO : UserForSignInForMobileNumberDTO) : Call<Token>

    @PATCH("api/Auth/ResetPassword")
    fun resetPassword (@Header("Authorization") token : String,@Body userForResetPasswordDTO: UserForResetPasswordDTO) : Call<Void>
}

interface UsersInterface{

}