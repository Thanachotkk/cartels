package com.internship.nilecon.cartels.MyParking

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.internship.nilecon.cartels.API.Parking
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.view_recycle_view_my_parking_item.view.*

class RecyclerViewMyParkingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var parkingList:ArrayList<Parking>? = null
    private var listener : RecyclerViewMyParkingAdapter.OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: RecyclerViewMyParkingAdapter.OnItemClickListener) {
        this.listener = onItemClickListener
    }

    fun setParkingList(parkingList: ArrayList<Parking>){
        this.parkingList = parkingList
    }

    fun removeItem(position: Int){
        parkingList!!.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recycle_view_my_parking_item, parent, false)
        return MyParkingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return parkingList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.textViewMyParkingName.text = parkingList!![position].parkingName
        holder.itemView.textViewZone.text =  parkingList!![position].zone
        holder.itemView.textViewPositionValue.text =  parkingList!![position].position
        holder.itemView.textViewBookingTime.text = parkingList!![position].bookingType  + " : "
        holder.itemView.textViewBookingTimeValue.text =  parkingList!![position].bookingTime
        holder.itemView.textViewBookingTimeValue.isSelected = true
        holder.itemView.textViewVehicleTypeValue.text =  parkingList!![position].vehicleType
        holder.itemView.buttonDelete.setOnClickListener {
            listener!!.onItemClick(position)
        }
        if(parkingList!![position].zone.isNullOrEmpty()){
            holder.itemView.textViewZone.visibility = View.INVISIBLE
            holder.itemView.textViewZone.visibility = View.INVISIBLE
            holder.itemView.textViewPositionValue.visibility =View.INVISIBLE
            holder.itemView.textViewPosition.visibility = View.INVISIBLE
        }else{
            holder.itemView.textViewZone.visibility = View.VISIBLE
            holder.itemView.textViewZone.visibility = View.VISIBLE
            holder.itemView.textViewPositionValue.visibility =View.VISIBLE
            holder.itemView.textViewPosition.visibility = View.VISIBLE
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position : Int)
    }

}

class MyParkingViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)


