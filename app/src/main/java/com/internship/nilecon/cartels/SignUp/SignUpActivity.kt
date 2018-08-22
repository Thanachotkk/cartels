package com.internship.nilecon.cartels.SignUp

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity()
        ,Step1Fragment.OnFragmentInteractionListener
        ,Step2Fragment.OnFragmentInteractionListener
        ,Step3Fragment.OnFragmentInteractionListener{

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setupButtonBack()
        setupFragmentSignUp()
    }

    private fun setupButtonBack(){
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupFragmentSignUp(){
        supportFragmentManager.addOnBackStackChangedListener {
            var tag = supportFragmentManager.findFragmentById(R.id.fragmentSignUp).javaClass.simpleName

            if (tag == Step1Fragment().javaClass.simpleName){
                textViewActionBar.text = "Step 1 / 4"
            }else if (tag == Step2Fragment().javaClass.simpleName){
                textViewActionBar.text = "Step 2 / 4"
            }else if (tag == Step3Fragment().javaClass.simpleName){
                textViewActionBar.text = "Step 3 / 4"
            }
        }
    }

}
