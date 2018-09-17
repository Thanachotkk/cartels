package com.internship.nilecon.cartels.ParkingDetail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.internship.nilecon.cartels.API.Rate
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.view_recycle_view_item_rate.view.*

class RecyclerViewRateAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var rateList : List<Rate>? = null

    fun setRateList (rateList : List<Rate>){
        this.rateList = rateList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recycle_view_item_rate, parent, false)
        return RateViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rateList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.textViewBookingFeeDaily.text = rateList!![position].Daily.toString()
        holder.itemView.textViewBookingFeeMonthly.text = rateList!![position].Monthly.toString()
        holder.itemView.textViewVehicycleTypeDaily.text = rateList!![position].VehicleType.toString()
        holder.itemView.textViewVehicycleTypeMonthly.text = rateList!![position].VehicleType.toString()
    }

}

class RateViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
    var textViewBookingFeeDaily = itemView.textViewBookingFeeDaily
    var textViewVehicycleTypeDaily = itemView.textViewVehicycleTypeDaily
    var textViewBookingFeeMonthly = itemView.textViewBookingFeeMonthly
    var textViewVehicycleTypeMonthly = itemView.textViewVehicycleTypeMonthly
}