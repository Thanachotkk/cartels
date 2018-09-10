package com.internship.nilecon.cartels.Main

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.view.LayoutInflater
import com.internship.nilecon.cartels.R


class SpinnerFilterVehicleAdapter(context: Context,inflter: LayoutInflater) : BaseAdapter() {
    private var context: Context = context
    private var inflter: LayoutInflater = inflter
    private lateinit var spinnerFilterVehicleList  : List<SpinnerFilterVehicle>

    fun setSpinnerFilterVehicleList(spinnerFilterVehicleList : List<SpinnerFilterVehicle>){
        this.spinnerFilterVehicleList = spinnerFilterVehicleList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = inflter.inflate(R.layout.view_spinner_items_vehicle_type,null)
        
        return view
    }

    override fun getItem(position: Int): Any {
        return spinnerFilterVehicleList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return  spinnerFilterVehicleList.size
    }
}