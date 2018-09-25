package com.internship.nilecon.cartels.Parkinglist.ViewHolder

import android.view.View
import android.widget.TextView
import com.internship.nilecon.cartels.R
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder

class ChildViewHolder (itemView: View) : ChildViewHolder(itemView) {

    private val artistName: TextView

    init {
        artistName = itemView.findViewById(R.id.Cartype) as TextView

    }

    fun setArtistName(name: String) {
        artistName.text = name
    }
}
