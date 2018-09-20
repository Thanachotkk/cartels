package com.internship.nilecon.cartels.MyParking

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.internship.nilecon.cartels.API.MyParkingList
import com.internship.nilecon.cartels.MyVehicle.RecyclerViewMyVehicleAdapter
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.view_recycle_view_my_parking_item.view.*
import kotlinx.android.synthetic.main.view_recycler_view_item_my_vehicle.view.*

class RecyclerViewMyParkingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var myParkingList:ArrayList<MyParkingList>? = null
    private var listener : RecyclerViewMyParkingAdapter.OnItemClickListener? = null

    interface OnItemClickListener{
        fun OnItemClick(position : Int)
    }
    fun setOnItemClickListener(onItemClickListener: RecyclerViewMyParkingAdapter.OnItemClickListener) {
        this.listener = onItemClickListener
    }

    fun setMyParkingList(myParkingList: ArrayList<MyParkingList>){
        this.myParkingList = myParkingList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recycle_view_my_parking_item, parent, false)
        return MyParkingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myParkingList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.textViewMyParkingName.text = myParkingList!![position].MyParkingName
        holder.itemView.textViewMyParkingZone.text =  myParkingList!![position].MyParkingZone
        holder.itemView.textViewMyParkingNumber.text =  myParkingList!![position].MyParkingNumber
        holder.itemView.textViewTitleMyParkingTime.text = myParkingList!![position].MyParkingTimeType
        holder.itemView.textViewMyParkingTime.text =  myParkingList!![position].MyParkingTime
        holder.itemView.textViewMyParkingOpenClose.text =  myParkingList!![position].MyParkingTimeOpenandClose
        holder.itemView.textViewMyParkingVehicleType.text =  myParkingList!![position].MyParkingVehicleType
        holder.itemView.buttonRemoveMyParking.setOnClickListener {
            listener!!.OnItemClick(position)
        }
        if(myParkingList!![position].MyParkingZone == "null"){
            holder.itemView.textViewMyParkingZone.visibility = View.GONE
            holder.itemView.textViewMyParkingTitleZone.visibility = View.GONE
            holder.itemView.textViewMyParkingNumber.visibility =View.GONE
            holder.itemView.textViewTitleMyParkingNumber.visibility = View.GONE
        }else{
            holder.itemView.textViewMyParkingZone.visibility = View.VISIBLE
            holder.itemView.textViewMyParkingTitleZone.visibility = View.VISIBLE
            holder.itemView.textViewMyParkingNumber.visibility =View.VISIBLE
            holder.itemView.textViewTitleMyParkingNumber.visibility = View.VISIBLE
        }
    }

}

class MyParkingViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)


