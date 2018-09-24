package com.internship.nilecon.cartels.MyVehicle

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_my_vehicle.*

class MyVehicleActivity : AppCompatActivity(),
        MyVehicleFragment.OnFragmentInteractionListener,
        AddMyVehicleFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vehicle)
        setupButtonAdd()
        setupButtonBack()
        setupFragmentVehicle()
    }
    private fun setupButtonBack(){
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }
    private fun setupFragmentVehicle() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentVehicle, MyVehicleFragment())
                .commit()

        supportFragmentManager.addOnBackStackChangedListener {
            var fragment = supportFragmentManager.findFragmentById(R.id.fragmentVehicle).javaClass.simpleName
            when (fragment) {
                MyVehicleFragment().javaClass.simpleName -> {
                    textViewActionBar.text = "My vehicle"
                    buttonAdd.visibility = View.VISIBLE
                }
                AddMyVehicleFragment().javaClass.simpleName -> {
                    textViewActionBar.text = "Add vehicle"
                    buttonAdd.visibility = View.GONE
                }
            }
        }
    }

    private fun setupButtonAdd() {
        buttonAdd.setOnClickListener {
            supportFragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right)
                    .replace(R.id.fragmentVehicle, AddMyVehicleFragment())
                    .addToBackStack(this.javaClass.name)
                    .commit()
        }
    }
}