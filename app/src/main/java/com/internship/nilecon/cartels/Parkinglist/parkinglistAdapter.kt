package com.internship.nilecon.cartels.Parkinglist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.internship.nilecon.cartels.Parkinglist.ViewHolder.ChildViewHolder
import com.internship.nilecon.cartels.Parkinglist.ViewHolder.GenreViewHolder
import com.internship.nilecon.cartels.Parkinglist.dataclass.child
import com.internship.nilecon.cartels.R
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class parkinglistAdapter(groups: List<ExpandableGroup<*>>) : ExpandableRecyclerViewAdapter<GenreViewHolder, ChildViewHolder>(groups) {

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
        val Child = group.items[childIndex] as child
        holder.setArtistName(Child.name!!)
    }

    override fun onBindGroupViewHolder(holder: GenreViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        holder.setGenreName(group.title)
    }

}
