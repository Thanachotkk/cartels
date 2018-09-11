package com.internship.nilecon.cartels.SignUp

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.internship.nilecon.cartels.API.*


import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_step2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Step2Fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Step2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Step2Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var mApi : Call<Void>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        TextPhoneNumber.text = ObjectAPi.GetPhoneAPI
        setupButtonNext()
        setupButtonNewOtp()
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onFragmentInteraction(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Step2Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                Step2Fragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun setupButtonNext() {
        activity!!.hideKeyboard(this!!.view!!) // ปิด keyboard
        buttonNext.setOnClickListener {

            val GetPhoneFromobject = ObjectAPi.GetPhoneAPI
            Log.i("PhoneNumber", GetPhoneFromobject)
            var OTP = editTextOtp.text.toString()

            if (editTextOtp.length() == 6) {
                ApiOTPCheck( UserForVerifyOtpDTO(GetPhoneFromobject, OTP, "SignUp"))
            } else {
                Toast.makeText(activity!!, "โปรดกรอก OTP ให้ครบ 6 ตัว", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupButtonNewOtp() {
        buttonNewOtp.setOnClickListener {
            reSendOTP(UserForSentOtpSmsForSignUpDTO(ObjectAPi.GetPhoneAPI))
        }
    }

    private fun reSendOTP(PhoneNumber: UserForSentOtpSmsForSignUpDTO) {
        mApi = Api().Declaration(activity!!, AuthenticationsInterface::class.java)
                .sentOtpSmsForSignUp(PhoneNumber)
        mApi!!.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                Toast.makeText(activity!!, "Fail Api", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                if (response!!.code() == 200) {
                    Toast.makeText(activity!!, "ส่ง OTP แล้ว", Toast.LENGTH_SHORT).show()
                }
                if (response!!.code() == 400) {
                    Toast.makeText(activity!!, "ไม่สามารถส่งได้", Toast.LENGTH_SHORT).show()
                    Log.i("Response", "$response")
                }
            }
        })
    }
    override fun onDestroyView() {  //เมื่อ fragment นี้ปิดตัวลง
        super.onDestroyView()
        if (mApi != null){ // ถ้า Api request ยังไม่สำเร็จ
            (mApi as Call<Void>).cancel() //ยกเลิก Api request
            activity!!.constraintLayoutLayoutLoading.visibility = View.GONE // ปิด Loading
        }
    }
    private fun ApiOTPCheck(OTP: UserForVerifyOtpDTO) {
        mApi = Api().Declaration(activity!!, AuthenticationsInterface::class.java)
                .verifyOtpForSignUp(OTP)
        mApi!!.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                Toast.makeText(activity!!, "API Fail", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                if (response!!.code() == 200){
                    NextFragment()
                }
                if (response!!.code() == 400) {
                    Log.i("Response", "$response")
                    Toast.makeText(activity!!, "OTP ไม่ถูกต้อง", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private fun NextFragment() {
        activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right)
                .replace(R.id.fragmentSignUp, Step3Fragment())
                .addToBackStack(this.javaClass.name)
                .commit()
    }
}