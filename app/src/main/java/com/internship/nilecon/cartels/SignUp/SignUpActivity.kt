package com.internship.nilecon.cartels.SignUp

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.internship.nilecon.cartels.R

class SignUpActivity : AppCompatActivity()
        ,Step1Fragment.OnFragmentInteractionListener
        ,Step2Fragment.OnFragmentInteractionListener{
    override fun onFragmentInteraction(uri: Uri) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }
}
