package com.internship.nilecon.cartels.SignUp

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import com.facebook.login.LoginManager
import com.internship.nilecon.cartels.API.UserForAddOrReplacePhotoDTO
import com.internship.nilecon.cartels.API.UserForSignUpDTO
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_sign_up.*

object SIGN_UP{
    var UserForSignUpDTO = com.internship.nilecon.cartels.API.UserForSignUpDTO(null,null,null,null ,null)
    var UserForAddOrReplacePhotoDTO = com.internship.nilecon.cartels.API.UserForAddOrReplacePhotoDTO(null)
}

class SignUpActivity : AppCompatActivity()
        ,Step1Fragment.OnFragmentInteractionListener
        ,Step2Fragment.OnFragmentInteractionListener
        ,Step3Fragment.OnFragmentInteractionListener
        ,Step4Fragment.OnFragmentInteractionListener{

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setupButtonBack()
        setupFragmentSignUp()
    }

    override fun onDestroy() {
        super.onDestroy()
    }



    private fun setupButtonBack(){
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupFragmentSignUp(){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentSignUp,Step1Fragment())
                .commit()

        supportFragmentManager.addOnBackStackChangedListener {
            var fragment = supportFragmentManager.findFragmentById(R.id.fragmentSignUp).javaClass.simpleName
            when (fragment) {
                Step1Fragment().javaClass.simpleName -> textViewActionBar.text = "Step 1 / 4"
                Step2Fragment().javaClass.simpleName -> textViewActionBar.text = "Step 2 / 4"
                Step3Fragment().javaClass.simpleName -> {
                    textViewActionBar.text = "Step 3 / 4"
                    LoginManager.getInstance().logOut()}
                Step4Fragment().javaClass.simpleName -> textViewActionBar.text = "Step 4 / 4"
            }
        }
    }

}
