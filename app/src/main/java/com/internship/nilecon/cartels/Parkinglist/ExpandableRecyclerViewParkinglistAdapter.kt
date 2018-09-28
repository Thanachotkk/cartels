package com.internship.nilecon.cartels.Parkinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.internship.nilecon.cartels.Parkinglist.DataClass.Child
import com.internship.nilecon.cartels.R
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class ExpandableRecyclerViewParkinglistAdapter(groups: List<ExpandableGroup<*>>) : ExpandableRecyclerViewAdapter<GenreViewHolder, ChildViewHolder>(groups) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_genre, parent, false)
        return GenreViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_child, parent, false)
        return ChildViewHolder(view)
    }

    override fun onBindChildViewHolder(holder: ChildViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        val child = group.items[childIndex] as Child
        holder.setArtistName(child.name!!)
    }

    override fun onBindGroupViewHolder(holder: GenreViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        holder.setGenreName(group.title)
    }

}

class ChildViewHolder (itemView: View) : com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder(itemView) {

    private val artistName: TextView = itemView.findViewById(R.id.textViewType) as TextView

    fun setArtistName(name: String) {
        artistName.text = name
    }
}

class GenreViewHolder(itemView: View) : GroupViewHolder(itemView) {

    private val genreTitle: TextView = itemView.findViewById(R.id.textViewFloorValue) as TextView

    fun setGenreName(name: String) {
        genreTitle.text = name
    }
}

