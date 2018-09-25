package com.internship.nilecon.cartels.Parkinglist.ViewHolder

import android.view.View
import android.widget.TextView
import com.internship.nilecon.cartels.R
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class GenreViewHolder(itemView: View) : GroupViewHolder(itemView) {

    private val genreTitle: TextView

    init {
        genreTitle = itemView.findViewById(R.id.list_item_genre_name) as TextView
    }

    fun setGenreName(name: String) {
        genreTitle.text = name
    }
}
