package com.internship.nilecon.cartels.SignIn

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.internship.nilecon.cartels.API.*

import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.fragment_forgot_password.*
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
 * [ForgotPasswordFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ForgotPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ForgotPasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mApi : Any? = null
    private var listener: OnFragmentInteractionListener? = null

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
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonNext()
        setupButtonNewOtp()
        setupEditTextOtp()
        setupTextViewMobileNumber()
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
         * @return A new instance of fragment ForgotPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ForgotPasswordFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }

    }

    private fun callApiForgotPassword(){
        TransitionManager.beginDelayedTransition(activity!!.constraintLayoutLayoutLoading)
        activity!!.constraintLayoutLayoutLoading.visibility = View.VISIBLE // เปิด Loading

        mApi = Api().Declaration(activity!!, AuthenticationsInterface::class.java)
                .forgotPasswor(UserForForgotPasswordDTO(SIGN_IN.UserForVerifyOtpDTO.MobileNumber))  //ตั้งค่า Api request

        (mApi as Call<Void>).enqueue(object : Callback<Void>{
            override fun onFailure(call: Call<Void>, t: Throwable) {
                TransitionManager.beginDelayedTransition(activity!!.constraintLayoutLayoutLoading)
                activity!!.constraintLayoutLayoutLoading.visibility = View.GONE
                print(t.message)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                TransitionManager.beginDelayedTransition(activity!!.constraintLayoutLayoutLoading)
                activity!!.constraintLayoutLayoutLoading.visibility = View.GONE
            }
        })
    }

    private fun callApiVerifyOpt(){
        TransitionManager.beginDelayedTransition(activity!!.constraintLayoutLayoutLoading)
        activity!!.constraintLayoutLayoutLoading.visibility = View.VISIBLE

        mApi = Api().Declaration(activity!!,AuthenticationsInterface::class.java)
                .verifyOtpForForgotPassword(UserForVerifyOtpDTO(SIGN_IN.UserForVerifyOtpDTO.MobileNumber
                ,SIGN_IN.UserForVerifyOtpDTO.Otp,SIGN_IN.UserForVerifyOtpDTO.VerifyFor))

        (mApi as Call<Token>).enqueue(object : Callback<Token>{
            override fun onFailure(call: Call<Token>, t: Throwable) {
                print(t.message)
                TransitionManager.beginDelayedTransition(activity!!.constraintLayoutLayoutLoading)
                activity!!.constraintLayoutLayoutLoading.visibility = View.GONE
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                TransitionManager.beginDelayedTransition(activity!!.constraintLayoutLayoutLoading)
                activity!!.constraintLayoutLayoutLoading.visibility = View.GONE

                when(response.code()){
                    200->{
                        var token = response.body()!!.Token //แปลง Token ที่ได้มาให้เป็น String

                        var perfs = activity!!.getSharedPreferences(getString(R.string.app_name)/*ตั้งชื่อของ SharedPreferences*/
                                ,Context.MODE_PRIVATE/*SharedPreferences แบบเห็นได้เฉพาะ app นี้เท่านั้น MODE_PRIVATE*/)
                                .edit()  // ประกาศใช้ SharedPreferences เพื่อเก็บ Token
                        perfs.putString("Token",token) /*เก็บ Token ลง SharedPreferences โดยอ้างชื่อว่า Token*/
                        perfs.commit() /*ยืนยันการบันทึก SharedPreferences*/

                        var intent = Intent(activity!!,ResetPasswordActivity::class.java)
                        startActivity(intent)
                        activity!!.finishAffinity()

                    }

                    400->{
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

    private fun setupTextViewMobileNumber(){
        textViewMobileNumber.text = SIGN_IN.UserForVerifyOtpDTO.MobileNumber
    }

    private fun setupEditTextOtp(){
        editTextOtp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length in 0..5) editTextOtp.error = "You must specify otp 6 characters"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun setupButtonNext(){
        buttonNext.setOnClickListener {


            activity!!.hideKeyboard(this!!.view!!) // ปิด keyboard

            if(editTextOtp.text.length in  0..5) //ถ้า editTextOtp ไม่ครบ 6 ตัว
                editTextOtp.error = "You must specify otp 6 characters" // แจ้ง error ที่ editTextOtp
            else  //ทำ function ส่งคำร้องขอ Api request ไปที่ Server
            {
                SIGN_IN.UserForVerifyOtpDTO.Otp = editTextOtp.text.toString()
                callApiVerifyOpt()
            }
        }
    }

    private fun setupButtonNewOtp(){
        buttonNewOtp.setOnClickListener {
            editTextOtp.text.clear()
            callApiForgotPassword()
        }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
