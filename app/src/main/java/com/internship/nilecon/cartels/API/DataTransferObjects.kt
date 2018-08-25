package com.internship.nilecon.cartels.API

import android.graphics.Bitmap
import java.io.File

data class UserForSentOtpSmsForSignUpDTO (var MobileNumber : String)

data class UserForVerifyOtpDTO(var MobileNumber : String
                            ,var Otp : String
                            ,var VerifyFor : String)

data class UserForSignUpDTO(var MobileNumber:String?
                            , var Name:String?
                            , var GoogleId:String?
                            , var FacebookId : String?
                            , var Photo : Bitmap?
                            , var Password : String?)