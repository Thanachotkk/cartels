package com.internship.nilecon.cartels.API

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationsInterface{

    @POST("api/Auth/SentOtpSmsForSignUp")
    fun sentOtpSmsForSignUp(@Body userForSentOtpSmsForSignUpDTO: UserForSentOtpSmsForSignUpDTO) : Call<Void>

    @POST("api/Auth/VerifyOtp")
    fun verifyOtp(@Body userForVerifyOtpDTO: UserForVerifyOtpDTO) : Call<Void>

}