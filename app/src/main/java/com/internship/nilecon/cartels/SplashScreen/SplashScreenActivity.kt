package com.internship.nilecon.cartels.SplashScreen

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.internship.nilecon.cartels.Introduction.Introduction
import com.internship.nilecon.cartels.Introduction.IntroductionActivity
import com.internship.nilecon.cartels.Main.MapsActivity
import com.internship.nilecon.cartels.R

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var delay_time: Long = 0
    private var time = 1500L

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler()

        runnable = Runnable {
            val prefs = getSharedPreferences(getString(R.string.app_name),Context.MODE_PRIVATE)
            var token  = prefs.getString("Token",null)

            when{
                token.isNullOrEmpty() -> {
                    startActivity(Intent(this, IntroductionActivity::class.java))
                    finish()
                }
                token.isNotEmpty() ->{
                    startActivity(Intent(this, MapsActivity::class.java))
                    finish()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        delay_time = time
        handler.postDelayed(runnable, delay_time)
        time = System.currentTimeMillis()
    }


    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
        time = delay_time - (System.currentTimeMillis() - time)
    }
}

