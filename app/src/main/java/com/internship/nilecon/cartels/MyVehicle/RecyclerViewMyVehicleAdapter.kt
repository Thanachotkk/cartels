package com.internship.nilecon.cartels.MyVehicle

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.internship.nilecon.cartels.API.MyVehicleList
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.view_recycler_view_item_my_vehicle.view.*

class RecyclerViewMyVehicleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listener : OnItemClickListener? = null
    interface OnItemClickListener{
        fun OnItemClick(position : Int)
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.listener = onItemClickListener
    }
    private var myVehicleList: ArrayList<MyVehicleList>? = null

    fun setMyVehicle(myVehicleList: ArrayList<MyVehicleList>) {
        this.myVehicleList = myVehicleList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recycler_view_item_my_vehicle, parent, false)
        return MyVehicleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myVehicleList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.textViewVehicleName.text = myVehicleList!![position].MyVehicleName
        holder.itemView.textViewVehicleProvince.text = myVehicleList!![position].MyVehicleProvince
        holder.itemView.textViewVehicleLicense.text = myVehicleList!![position].MyVehicleLicense
        holder.itemView.buttonDeleteVehicle.setOnClickListener {
            listener!!.OnItemClick(position)
        }
    }
    fun Delete(position: Int){
        myVehicleList!!.removeAt(position)
        notifyItemRemoved(position)
    }
}

class MyVehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textVehicleName = itemView.textViewVehicleName
    var textVehicleLicense = itemView.textViewVehicleLicense
    var textVehicleProvince = itemView.textViewVehicleProvince
}