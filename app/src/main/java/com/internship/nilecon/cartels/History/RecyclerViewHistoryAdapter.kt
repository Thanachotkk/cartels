package com.internship.nilecon.cartels.History

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.internship.nilecon.cartels.API.History
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.view_recycler_view_item_history.view.*

class RecyclerViewHistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var historyList: ArrayList<History>? = null

    fun setHistoryList(historyList: ArrayList<History>){
        this.historyList = historyList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recycler_view_item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.textViewName.text = historyList!![position].name
        holder.itemView.textViewPrice.text = historyList!![position].price
        holder.itemView.textViewBookingTimeValue.text = historyList!![position].bookingTime
        holder.itemView.textViewBookingTime.text  = historyList!![position].bookingType
       when(historyList!![position].vehicleType){
           "Car" -> holder.itemView.imageViewHistoryVehicle.setImageResource(R.drawable.vt_circle_car)
           "Bigbike" -> holder.itemView.imageViewHistoryVehicle.setImageResource(R.drawable.vt_circle_big_bike)
           "Motorcycle" -> holder.itemView.imageViewHistoryVehicle.setImageResource(R.drawable.vt_circle_motorcycle)
       }

    }

}

class HistoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

