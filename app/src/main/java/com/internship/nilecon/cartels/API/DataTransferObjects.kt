package com.internship.nilecon.cartels.API

import android.graphics.Bitmap
import android.net.Uri
import java.io.File


//data class for Request
data class UserForSentOtpSmsForSignUpDTO (var MobileNumber : String?)

data class UserForVerifyOtpDTO(var MobileNumber : String?
                            ,var Otp : String?
                            ,var VerifyFor : String?)

data class UserForSignUpDTO(var MobileNumber:String?
                            , var Name:String?
                            , var GoogleId:String?
                            , var FacebookId : String?
                            , var Password : String?)

data class UserForAddOrReplacePhotoDTO(var Photo : File?)

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

data class ParkingForGetParkingPointByLatLngDTO(
        var Latitude: Double?,
        var Longitude: Double?,
        var Radius: Int?,
        var VehicleType: String?
)

data class UserForResetPasswordDTO(var MobileNumber:String?, var Password : String?)


data class ParkingForGetParkingDetailDTO(
    var ParkingId: Int?
)

//data class for response
data class Token(var Token : String?)

data class ParkingPoint(
    var ParkingId: Int?,
    var Title: String?,
    var Latitude: Double?,
    var Longitude: Double?,
    var Type: String?
)


data class ParkingDetail(
    var ParkingId: Int?,
    var Title: String?,
    var Latitude: Double?,
    var Longitude: Double?,
    var Address: String?,
    var OpenTime: String?,
    var CloseTime: String?,
    var Type: String?,
    var PhotoTitleUrl: String?,
    var PhotoBannerUrl: String?,
    var Nearby: String?,
    var Note: String?,
    var Tel: String?,
    var Supports: List<String?>?,
    var Rates: List<Rate?>?,
    var Amenities: List<Amenity?>?
)

data class Rate(
    var VehicleType: String?,
    var Daily: Int?,
    var Monthly: Int?
)

data class Amenity(
    var EvCharger: Boolean?,
    var CarWash: Boolean?,
    var AirportShuttle: Boolean?,
    var Disabled: Boolean?,
    var Restrooms: Boolean?,
    var Security: Boolean?
)
