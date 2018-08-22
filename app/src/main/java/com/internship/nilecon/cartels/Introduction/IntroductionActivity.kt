package com.internship.nilecon.cartels.Introduction

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.widget.ImageView
import android.widget.LinearLayout
import com.internship.nilecon.cartels.R
import com.internship.nilecon.cartels.SignIn.SignInActivity
import com.internship.nilecon.cartels.SignUp.SignUpActivity
import kotlinx.android.synthetic.main.activity_introduction.*
import java.util.*

class IntroductionActivity : AppCompatActivity() {

    private lateinit var dot : Array<ImageView?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)


        setupViewPager()
        setupButtonSignUp()
        setupButtonSignIn()
    }

    private fun setupViewPager(){
        var viewPagerIntroductionAdapter = ViewPagerIntroductionAdapter(this)
        viewPagerIntroductionAdapter.setIntroductionList(getIntroductionList())
        viewPager.adapter = viewPagerIntroductionAdapter

        dot = arrayOfNulls<ImageView>(viewPagerIntroductionAdapter!!.count)

        for (i in 0 until viewPagerIntroductionAdapter!!.count){
            dot!![i] = ImageView(this)
            dot!![i]!!.setImageResource(R.drawable.ic_not_active_dot)
            var params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(8,0,8,0)
            linearLayoutSliderDots!!.addView(dot!![i],params)
        }
        dot!![0]!!.setImageResource(R.drawable.ic_active_dot)

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                for (i in 0 until viewPagerIntroductionAdapter!!.count){
                    dot!![i]!!.setImageResource(R.drawable.ic_not_active_dot)
                }
                dot!![position]!!.setImageResource(R.drawable.ic_active_dot)
            }
        })

        Timer().scheduleAtFixedRate(AutoSlider(this,dot.size),2000,4000)
    }

    private fun getIntroductionList() : List<Introduction>{
        val introductionList = ArrayList<Introduction>()
        introductionList.add(Introduction(R.drawable.im_intro1
                ,"ทำชีวิตคุณง่ายขึ้น"
                ,"ที่จอดรถง่าย ๆ ผ่าน Cartal"))
        introductionList.add(Introduction(R.drawable.im_intro1
                ,"ประหยัดเวลามากขึ้น"
                ,"ไม่ต้องวนหาที่จอด เพิ่มเวลาช้อป เวลาชิว"))
        introductionList.add(Introduction(R.drawable.im_intro1
                ,"จ่ายง่ายไม่เสียเวลา"
                ,"เลือกจ่ายได้ไม่ต้องกังวล เพียงเติมเงินหรือหักผ่านบัตรเครดิต"))
        return  introductionList
    }

    private fun setupButtonSignUp(){
        buttonSignUp.setOnClickListener {
            var intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupButtonSignIn(){
        buttonSignIn.setOnClickListener {
            var intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}

class AutoSlider(activity: Activity, countOfImage: Int) : TimerTask() {
    private var activity : Activity? = null
    private var countOfImage : Int? = null

    init {
        this.activity = activity
        this.countOfImage = countOfImage
    }

    override fun run() {
        this.activity!!.runOnUiThread {
            if (activity!!.viewPager.currentItem >= 0 && activity!!.viewPager.currentItem < countOfImage!! -1){
                activity!!.viewPager.currentItem = activity!!.viewPager.currentItem +1
            }else{
                activity!!.viewPager.currentItem = 0
            }
        }
    }
}
