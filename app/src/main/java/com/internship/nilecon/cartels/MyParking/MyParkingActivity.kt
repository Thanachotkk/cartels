package com.internship.nilecon.cartels.MyParking

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_my_parking.*

class MyParkingActivity : AppCompatActivity(),MyParkingFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_parking)
        setupFragmentMyParking()
        setupAddMyParking()
        setupButtonBack()
    }

    private fun setupButtonBack() {
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupAddMyParking() {
        buttonAdd.setOnClickListener{
            onBackPressed()
        }
    }

    private fun setupFragmentMyParking() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentMyParking, MyParkingFragment())
                .commit()
    }
}
