package com.internship.nilecon.cartels.SignIn

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.internship.nilecon.cartels.API.UserForVerifyOtpDTO
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_sign_in.*

object SIGN_IN{
    var UserForVerifyOtpDTO =  com.internship.nilecon.cartels.API.UserForVerifyOtpDTO(null,null,null)

}

class SignInActivity : AppCompatActivity()
        ,SignInFragment.OnFragmentInteractionListener
        ,ForgotPasswordFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setupButtonBack()
        setupFragmentSignUp()
    }

    private fun setupButtonBack(){
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupFragmentSignUp(){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentSignIn, SignInFragment())
                .commit()

        supportFragmentManager.addOnBackStackChangedListener {
            var fragment = supportFragmentManager.findFragmentById(R.id.fragmentSignIn).javaClass.simpleName
            when (fragment) {
                SignInFragment().javaClass.simpleName -> textViewActionBar.text = "Sign in"
                ForgotPasswordFragment().javaClass.simpleName -> textViewActionBar.text = "Forgot password"
            }
        }
    }
}
