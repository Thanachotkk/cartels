package com.internship.nilecon.cartels.Main

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.view.LayoutInflater
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.view_spinner_items_vehicle_type.view.*


class SpinnerFilterVehicleAdapter(context: Context) : BaseAdapter() {
    private var context: Context = context
    private lateinit var spinnerFilterVehicleList  : List<SpinnerFilterVehicle>

    fun setSpinnerFilterVehicleList(spinnerFilterVehicleList : List<SpinnerFilterVehicle>){
        this.spinnerFilterVehicleList = spinnerFilterVehicleList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.view_spinner_items_vehicle_type,null)
        var viewHolder = SpinnerItemsVehicleTypeViewHolder(view)

        viewHolder.imageViewVehicleType.setImageResource(this.spinnerFilterVehicleList[position].imageViewVehicleType)
        viewHolder.textViewVehicleType.text = this.spinnerFilterVehicleList[position].textViewVehicleType

        view?.tag = viewHolder

        return  view
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

class SpinnerItemsVehicleTypeViewHolder (itemView: View){
    var textViewVehicleType: TextView = itemView.textViewVehicleType
    var imageViewVehicleType : ImageView = itemView.imageViewVehicleType
}