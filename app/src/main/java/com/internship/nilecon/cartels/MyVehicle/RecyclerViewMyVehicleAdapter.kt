package com.internship.nilecon.cartels.MyVehicle

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.internship.nilecon.cartels.API.Vehicle
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.view_recycler_view_item_my_vehicle.view.*

class RecyclerViewMyVehicleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listener : OnItemClickListener? = null
    private var vehicleList: ArrayList<Vehicle>? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.listener = onItemClickListener
    }

    fun setVehicleList(vehicleList: ArrayList<Vehicle>) {
        this.vehicleList = vehicleList
    }

    fun removeItem(position: Int){
        vehicleList!!.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recycler_view_item_my_vehicle, parent, false)
        return MyVehicleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return vehicleList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.textViewVehicleName.text = vehicleList!![position].vehicleName
        holder.itemView.textViewVehicleProvinceValue.text = vehicleList!![position].province
        holder.itemView.textViewVehicleLicenseValue.text = vehicleList!![position].license
        holder.itemView.buttonDelete.setOnClickListener {
            listener!!.onItemClick(position)
        }

        when(vehicleList!![position].vehicleType){
            "Car" -> holder.itemView.imageViewVehicle.setImageResource(R.drawable.vt_car)
            "Motorcycle" -> holder.itemView.imageViewVehicle.setImageResource(R.drawable.vt_motorcycle)
            "Bigbike" -> holder.itemView.imageViewVehicle.setImageResource(R.drawable.vt_big_bike)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position : Int)
    }

}

class MyVehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

