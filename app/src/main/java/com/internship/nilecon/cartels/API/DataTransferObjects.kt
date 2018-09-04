package com.internship.nilecon.cartels.API

import android.graphics.Bitmap
import java.io.File

data class UserForSentOtpSmsForSignUpDTO (var MobileNumber : String?)

data class UserForVerifyOtpDTO(var MobileNumber : String?
                            ,var Otp : String?
                            ,var VerifyFor : String?)

data class UserForSignUpDTO(var MobileNumber:String?
                            , var Name:String?
                            , var GoogleId:String?
                            , var FacebookId : String?
                            , var Password : String?)

data class UserForAddOrReplacePhotoDTO(var Photo : Bitmap?)

data class UserForSignInForMobileNumberDTO(
    var MobileNumber: String?,
    var Password: String?
)

data class UserForForgotPasswordDTO(var MobileNumber: String?)

data class UserForIsUserExistsBySocialDTO(
        var SocialId: String?,
        var SocialType: String?
)

data class UserForSignInSocialDTO(
        var SocialId: String?,
        var SocialType: String?
)

data class UserForResetPasswordDTO(var MobileNumber:String?, var Password : String?)



data class Token(var Token : String?)