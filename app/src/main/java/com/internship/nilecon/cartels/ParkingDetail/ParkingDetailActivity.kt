package com.internship.nilecon.cartels.ParkingDetail

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import com.bumptech.glide.Glide
import com.internship.nilecon.cartels.API.ParkingDetail
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_parking_detail.*

object PARKING_DETAIL {
    var parkingDetail: ParkingDetail? = null
}

class ParkingDetailActivity : AppCompatActivity(),
        RatesFragment.OnFragmentInteractionListener,
        AmenitiesFragment.OnFragmentInteractionListener,
        NearbyFragment.OnFragmentInteractionListener,
        NoteFragment.OnFragmentInteractionListener {


    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_detail)

        var parkingDetail = intent.getParcelableExtra<ParkingDetail>("parkingDetail")
        PARKING_DETAIL.parkingDetail = parkingDetail

        setupButtonBack()
        setupViewPagerAndTabLayout()
        setupParkingDetail(parkingDetail!!)
    }

    private fun setupButtonBack() {
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupViewPagerAndTabLayout() {
        viewPagerParkingDetail.adapter = ParkingDeteilFragmentPagerAdapter(supportFragmentManager)
        viewPagerParkingDetail.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayoutParkingDetail))
        tabLayoutParkingDetail.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPagerParkingDetail))
    }

    private fun setupParkingDetail(parkingDetail: ParkingDetail) {

        Glide.with(this).load(parkingDetail.PhotoTitleUrl).into(imageViewTitle)

        Glide.with(this).load(parkingDetail.PhotoBannerUrl).into(imageViewHeader)

        textViewTitle.text = parkingDetail.Title;textViewTitle.isSelected = true

        textViewAddressValue.text = parkingDetail.Address;textViewAddressValue.isSelected = true

        if (parkingDetail.OpenTime == parkingDetail.CloseTime) {
            textViewOpenAndCloseValue.text = "24 hour";textViewOpenAndCloseValue.isSelected = true
        } else {
            textViewOpenAndCloseValue.text = parkingDetail.OpenTime!!.substring(0, 5) + " - " + parkingDetail.CloseTime!!.substring(0, 5);textViewOpenAndCloseValue.isSelected = true
        }

        parkingDetail.Supports!!.forEachIndexed { index, s ->
            if (index == 0) textViewSupportValue.text = s
            else textViewSupportValue.text = textViewSupportValue.text.toString() + ", $s"
        };textViewSupportValue.isSelected = true

    }

}
