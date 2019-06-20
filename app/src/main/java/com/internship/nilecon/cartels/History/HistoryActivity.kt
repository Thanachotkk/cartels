package com.internship.nilecon.cartels.History

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() ,HistoryFragment.OnFragmentInteractionListener{
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setupFragmentHistory()
        setupButtonBack()
    }
//
    private fun setupButtonBack() {
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupFragmentHistory() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentHistory, HistoryFragment())
                .commit()
    }
}
