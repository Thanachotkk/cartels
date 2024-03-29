package com.internship.nilecon.cartels.Profile

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.internship.nilecon.cartels.R
import com.internship.nilecon.cartels.SignIn.ResetPasswordFragment
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(), ProfileFragment.OnFragmentInteractionListener,
        ProfileNameFragment.OnFragmentInteractionListener,
        ProfileMobileNumberFragment.OnFragmentInteractionListener,
        ProfilePasswordFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setFragmentProfile()
        setupButtonBack()
    }

    private fun setupButtonBack(){
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }
    private fun setFragmentProfile() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentProfile, ProfileFragment())
                .commit()


        supportFragmentManager.addOnBackStackChangedListener {
            var fragment = supportFragmentManager.findFragmentById(R.id.fragmentProfile).javaClass.simpleName
            when (fragment) {
                ProfileNameFragment().javaClass.simpleName -> textViewActionBar.text = "Name"
                ProfileMobileNumberFragment().javaClass.simpleName -> textViewActionBar.text = "Mobile number"
                ProfilePasswordFragment().javaClass.simpleName -> textViewActionBar.text = "Password"
            }
        }
    }
}
