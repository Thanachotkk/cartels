package com.internship.nilecon.cartels.API

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationsInterface{

    @POST("api/Auth/SentOtpSmsForSignUp")
    fun sentOtpSmsForSignUp(@Body userForSentOtpSmsForSignUpDTO: UserForSentOtpSmsForSignUpDTO) : Call<Void>

    @POST("api/Auth/VerifyOtp")
    fun verifyOtp(@Body userForVerifyOtpDTO: UserForVerifyOtpDTO) : Call<Void>

    @POST("api/Auth/SignUp")
    fun signUp(@Body userForSignUpDTO: UserForSignUpDTO) : Call<Token>

    @POST("api/Auth/SignInForMobileNumber")
    fun signInForMobileNumber(@Body userForSignInForMobileNumberDTO : UserForSignInForMobileNumberDTO) : Call<Token>

    @POST("api/Auth/SignInForSocial")
    fun signInForSocial(@Body userForSignInForSocialDTO : UserForSignInForMobileNumberDTO) : Call<Token>

}