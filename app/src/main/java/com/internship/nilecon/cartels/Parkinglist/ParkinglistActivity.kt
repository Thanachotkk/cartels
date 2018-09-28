package com.internship.nilecon.cartels.Parkinglist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_history.*

class ParkinglistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parkinglist)
        setupButtonBack()
        setupFragmentParkinglist()
    }

    private fun setupButtonBack() {
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupFragmentParkinglist() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentParkinglist, ParkinglistsFragment())
                .commit()
    }
}


