package com.internship.nilecon.cartels.History

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.internship.nilecon.cartels.API.HistoryList
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.view_recycler_view_item_history.view.*

class RecyclerViewHistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var historyArrayList: ArrayList<HistoryList>? = null

    fun setHistory(historyArrayList: ArrayList<HistoryList>){
        this.historyArrayList = historyArrayList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recycler_view_item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyArrayList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       holder.itemView.textViewParkNameHistory.text = historyArrayList!![position].HistoryParkName
        holder.itemView.textViewPrice.text = historyArrayList!![position].HistoryPrice
        holder.itemView.textViewTimeHistory.text = historyArrayList!![position].HistoryTime
       when(historyArrayList!![position].HistoryVehicleType){
           "Car" -> holder.itemView.imageViewHistoryVehicle.setImageResource(R.drawable.vt_circle_car)
           "BigBike" -> holder.itemView.imageViewHistoryVehicle.setImageResource(R.drawable.vt_circle_big_bike)
           "Motorcycle" -> holder.itemView.imageViewHistoryVehicle.setImageResource(R.drawable.vt_circle_motorcycle)
       }

    }

}

class HistoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

