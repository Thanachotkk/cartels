package com.internship.nilecon.cartels.API

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
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

data class ParkingForGetParkingPointsDTO(
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.createTypedArrayList(Rate),
            parcel.createTypedArrayList(Amenity)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(ParkingId)
        parcel.writeString(Title)
        parcel.writeValue(Latitude)
        parcel.writeValue(Longitude)
        parcel.writeString(Address)
        parcel.writeString(OpenTime)
        parcel.writeString(CloseTime)
        parcel.writeString(Type)
        parcel.writeString(PhotoTitleUrl)
        parcel.writeString(PhotoBannerUrl)
        parcel.writeString(Nearby)
        parcel.writeString(Note)
        parcel.writeString(Tel)
        parcel.writeStringList(Supports)
        parcel.writeTypedList(Rates)
        parcel.writeTypedList(Amenities)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParkingDetail> {
        override fun createFromParcel(parcel: Parcel): ParkingDetail {
            return ParkingDetail(parcel)
        }

        override fun newArray(size: Int): Array<ParkingDetail?> {
            return arrayOfNulls(size)
        }
    }
}

data class Rate(
    var VehicleType: String?,
    var Daily: Int?,
    var Monthly: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(VehicleType)
        parcel.writeValue(Daily)
        parcel.writeValue(Monthly)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Rate> {
        override fun createFromParcel(parcel: Parcel): Rate {
            return Rate(parcel)
        }

        override fun newArray(size: Int): Array<Rate?> {
            return arrayOfNulls(size)
        }
    }
}

data class Amenity(
    var EvCharger: Boolean?,
    var CarWash: Boolean?,
    var AirportShuttle: Boolean?,
    var Disabled: Boolean?,
    var Restrooms: Boolean?,
    var Security: Boolean?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(EvCharger)
        parcel.writeValue(CarWash)
        parcel.writeValue(AirportShuttle)
        parcel.writeValue(Disabled)
        parcel.writeValue(Restrooms)
        parcel.writeValue(Security)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Amenity> {
        override fun createFromParcel(parcel: Parcel): Amenity {
            return Amenity(parcel)
        }

        override fun newArray(size: Int): Array<Amenity?> {
            return arrayOfNulls(size)
        }
    }
}

data class PaymentCard(var cardNumber : String, var name :String, var cvv :String,var expiry :String)

data class Vehicle(var vehicleName: String, var license: String, var province: String,var vehicleType : String)
