package com.internship.nilecon.cartels.Introduction

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.internship.nilecon.cartels.R

class ViewPagerIntroductionAdapter (context : Context) : PagerAdapter() {
    private lateinit var introductionList : List<Introduction>
    private var context : Context = context


    fun setIntroductionList (introductionList : List<Introduction>){
        this.introductionList = introductionList
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return this.introductionList!!.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = inflater.inflate(R.layout.view_pager_introduction,null)

        var imageViewPromotion = view.findViewById<View>(R.id.imageViewIntroduction) as ImageView
        imageViewPromotion.setImageResource(introductionList!![position].imageViewIntroduction)

        var textViewIntroductionHeader = view.findViewById<View>(R.id.textViewIntroductionHeader) as TextView
        textViewIntroductionHeader.text = introductionList!![position].textViewIntroductionHeader

        var textViewIntroductionDetail = view.findViewById<View>(R.id.textViewIntroductionDetail) as TextView
        textViewIntroductionDetail.text = introductionList!![position].textViewIntroductionDetail

        var viewPager = container as ViewPager
        viewPager.addView(view,0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}