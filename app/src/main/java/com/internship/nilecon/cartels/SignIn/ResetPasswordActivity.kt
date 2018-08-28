package com.internship.nilecon.cartels.SignIn

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.internship.nilecon.cartels.Main.MapsActivity
import com.internship.nilecon.cartels.R

class ResetPasswordActivity : AppCompatActivity() ,ResetPasswordFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        setupFragmentResetPassword()
    }

    private fun setupFragmentResetPassword(){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentResetPassword, ResetPasswordFragment())
                .commit()
    }

    override fun onBackPressed() {
        var intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
        this.finishAffinity()
    }
}
