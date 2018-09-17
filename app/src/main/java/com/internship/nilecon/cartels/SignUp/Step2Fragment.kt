package com.internship.nilecon.cartels.SignUp

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.internship.nilecon.cartels.API.Api
import com.internship.nilecon.cartels.API.AuthenticationsInterface
import com.internship.nilecon.cartels.API.UserForSentOtpSmsForSignUpDTO
import com.internship.nilecon.cartels.API.UserForVerifyOtpDTO

import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_step2.*
import okhttp3.MediaType
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
    private var mApi : Any? = null

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

        setupTextViewMobileNumber()
        setupButtonNext()
        setupButtonNewOtp()
        setupEditTextOtp()
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

    override fun onDestroyView() {  //เมื่อ fragment นี้ปิดตัวลง
        super.onDestroyView()

        if (mApi != null){ // ถ้า Api request ยังไม่สำเร็จ
            (mApi as Call<Void>).cancel() //ยกเลิก Api request
            activity!!.constraintLayoutLayoutLoading.visibility = View.GONE // ปิด Loading
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
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

    private fun callApiSentOptSmsForSignUp(){
        TransitionManager.beginDelayedTransition(activity!!.constraintLayoutLayoutLoading)
        activity!!.constraintLayoutLayoutLoading.visibility = View.VISIBLE // เปิด Loading

        mApi = Api().Declaration(activity!!, AuthenticationsInterface::class.java)
                .sentOtpSmsForSignUp(UserForSentOtpSmsForSignUpDTO(SIGN_UP.UserForSignUpDTO.MobileNumber.toString()))  //ตั้งค่า Api request

        (mApi as Call<Void>).enqueue(object : Callback<Void> {  //ส่งคำร้องขอ Api request ไปที่ Server

            override fun onFailure(call: Call<Void>, t: Throwable) { //เมื่อ Server ตอบกลับแบบล้มเหลว
                TransitionManager.beginDelayedTransition(activity!!.constraintLayoutLayoutLoading)
                activity!!.constraintLayoutLayoutLoading.visibility = View.GONE //ปิด Loading
                print(t.message)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) { //เมื่อ Server ตอบกลับแบบสำเร็จ.
                TransitionManager.beginDelayedTransition(activity!!.constraintLayoutLayoutLoading)
                activity!!.constraintLayoutLayoutLoading.visibility = View.GONE //ปิด Loading
                when(response.code()){
                    200->{

                    }
                    400->{
                        when(response.errorBody()!!.contentType()){ //ตรวจ ประเภทของ errorBody

                            MediaType.parse("application/json; charset=utf-8") -> { //เมื่อ errorBody เป็นประเภท json
                                var jObjError = JSONObject(response.errorBody()!!.string())
                                print(jObjError.toString()) // error ที่เกิดขึ้น
                            }

                            MediaType.parse("text/plain; charset=utf-8") ->{ //เมื่อ errorBody เป็นประเภท text
                                print(response.errorBody()!!.charStream().readText()) //error ที่เกิดขึ้น
                            }
                        }
                    }
                }
            }
        })
    }

    private fun callApiVerify() {
        TransitionManager.beginDelayedTransition(activity!!.constraintLayoutLayoutLoading)
        activity!!.constraintLayoutLayoutLoading.visibility = View.VISIBLE // เปิด Loading

        mApi = Api().Declaration(activity!!,AuthenticationsInterface::class.java)
                .verifyOtpForSignUp(UserForVerifyOtpDTO(
                        SIGN_UP.UserForSignUpDTO.MobileNumber.toString()
                        ,editTextOtp.text.toString()
                        ,"SignUp"))

        (mApi as Call<Void>).enqueue(object : Callback<Void>{
            override fun onFailure(call: Call<Void>, t: Throwable) {
                TransitionManager.beginDelayedTransition(activity!!.constraintLayoutLayoutLoading)
                activity!!.constraintLayoutLayoutLoading.visibility = View.GONE //ปิด Loading
                print(t.message)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                TransitionManager.beginDelayedTransition(activity!!.constraintLayoutLayoutLoading)
                activity!!.constraintLayoutLayoutLoading.visibility = View.GONE //ปิด Loading

                when(response.code()){ //ตรวจ status code

                    200 -> { //เมื่อ status code : 200 (Ok).
                        activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
                                R.anim.enter_from_right,
                                R.anim.exit_to_left,
                                R.anim.enter_from_left,
                                R.anim.exit_to_right)
                                .replace(R.id.fragmentSignUp,Step3Fragment())
                                .addToBackStack(this.javaClass.name)
                                .commit() // ไป Step3Fragment
                    }

                    400 -> {  //เมื่อ status code : 400 (Bad request)

                        when(response.errorBody()!!.contentType()){ //ตรวจ ประเภทของ errorBody

                            MediaType.parse("application/json; charset=utf-8") -> { //เมื่อ errorBody เป็นประเภท json
                                var jObjError = JSONObject(response.errorBody()!!.string())
                                print(jObjError.toString()) // error ที่เกิดขึ้น
                            }

                            MediaType.parse("text/plain; charset=utf-8") ->{ //เมื่อ errorBody เป็นประเภท text
                                editTextOtp.error = response.errorBody()!!.charStream().readText()
                                print(response.errorBody()!!.charStream().readText()) //error ที่เกิดขึ้น
                            }
                        }
                    }
                }
            }
        })

    }

    private fun setupEditTextOtp(){
        editTextOtp.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length in 0..5) editTextOtp.error = "You must specify otp 6 characters"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun setupTextViewMobileNumber(){
        textViewMobileNumber.text = SIGN_UP.UserForSignUpDTO.MobileNumber
    }

    private fun setupButtonNext(){
        buttonNext.setOnClickListener {

            activity!!.hideKeyboard(this!!.view!!) // ปิด keyboard

            if(editTextOtp.text.length in  0..5) //ถ้า editTextOtp ไม่ครบ 6 ตัว
                editTextOtp.error = "You must specify otp 6 characters" // แจ้ง error ที่ editTextOtp
            else callApiVerify() //ทำ function ส่งคำร้องขอ Api request ไปที่ Server

        }
    }

    private fun setupButtonNewOtp(){
        buttonNewOtp.setOnClickListener {
            editTextOtp.text.clear()
            callApiSentOptSmsForSignUp()
        }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
