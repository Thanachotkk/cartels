package com.internship.nilecon.cartels.SignIn

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.internship.nilecon.cartels.API.*
import com.internship.nilecon.cartels.Main.MainActivity

import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
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
 * [SignInFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SignInFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupForgotPassword()
        setupButtonGoogle()
        setupButtonFacebook()
        setupButtonContinue()
        setupEditTextMobileNumber()
        setupEditTextPassword()
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
            activity!!.relativeLayoutLoading.visibility = View.GONE // ปิด Loading
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
         * @return A new instance of fragment SignInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SignInFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun callApiSignInForMobileNumber(){

        activity!!.relativeLayoutLoading.visibility = View.VISIBLE

        mApi = Api().Declaration(activity!!,AuthenticationsInterface::class.java)
                .signInForMobileNumber(UserForSignInForMobileNumberDTO(
                        editTextMobileNumber.text.toString()
                        ,editTextPassword.text.toString()))

        (mApi as Call<Token>).enqueue(object : Callback<Token>{
            override fun onFailure(call: Call<Token>, t: Throwable) {
                activity!!.relativeLayoutLoading.visibility = View.GONE //ปิด Loading
                print(t.message)
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                activity!!.relativeLayoutLoading.visibility = View.GONE

                when(response.code()){
                    200 -> {

                        var token = response.body()!!.token //แปลง Token ที่ได้มาให้เป็น String

                        var editor = activity!!.getSharedPreferences(getString(R.string.app_name)/*ตั้งชื่อของ SharedPreferences*/
                                ,Context.MODE_PRIVATE/*SharedPreferences แบบเห็นได้เฉพาะ app นี้เท่านั้น MODE_PRIVATE*/)
                                .edit()  // ประกาศใช้ SharedPreferences เพื่อเก็บ Token
                        editor.putString("Token",token) /*เก็บ token ลง SharedPreferences โดยอ้างชื่อว่า Token*/
                        editor.commit() /*ยืนยันการบันทึก SharedPreferences*/

                        var intent = Intent(activity!!, MainActivity::class.java)
                        startActivity(intent)
                        activity!!.finishAffinity()

                    }

                    400 -> {  //เมื่อ status code : 400 (Bad request)
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

                    401 -> {  //เมื่อ status code : 401 (Unauthorized)
                        editTextPassword.text.clear()
                        editTextPassword.error = "The Mobile number or password is incorrect"
                    }
                }
            }
        })
    }

    private fun callApiForgotPassword(){
        activity!!.relativeLayoutLoading.visibility = View.VISIBLE

        mApi = Api().Declaration(activity!!,AuthenticationsInterface::class.java)
                .forgotPasswor(UserForForgotPasswordDTO(editTextMobileNumber.text.toString()))

        (mApi as Call<Void>).enqueue(object : Callback<Void>{
            override fun onFailure(call: Call<Void>, t: Throwable) {
                activity!!.relativeLayoutLoading.visibility = View.GONE //ปิด Loading
                print(t.message)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                activity!!.relativeLayoutLoading.visibility = View.GONE

                when(response.code()){
                    200 -> {
                        SIGN_IN.UserForVerifyOtpDTO.MobileNumber = editTextMobileNumber.text.toString()
                        SIGN_IN.UserForVerifyOtpDTO.VerifyFor = "ForgotPassword"

                        activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right)
                        .replace(R.id.fragmentSignIn, ForgotPasswordFragment())
                        .addToBackStack(this.javaClass.name)
                        .commit()
                    }
                    400 -> {
                        editTextMobileNumber.error = "Mobile Number not found"

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

    private fun setupEditTextMobileNumber(){
        editTextMobileNumber.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length in 0..9) editTextMobileNumber.error = "You must specify mobile number 10 characters"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun setupEditTextPassword(){
        editTextPassword.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length !in 4..12) editTextPassword.error = "You must specify password between 4 - 12 characters"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun setupForgotPassword(){

        textViewForgotPassword.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        textViewForgotPassword.setOnClickListener {
            activity!!.hideKeyboard(this!!.view!!) // ปิด keyboard
            if(editTextMobileNumber.text.length in  0..9){
                editTextMobileNumber.error = "You must specify mobile number 10 characters"
            }else{
                callApiForgotPassword()
            }

        }
    }

    private fun setupButtonGoogle(){
        buttonGoogle.setOnClickListener {

        }
    }

    private fun setupButtonFacebook(){
        buttonFacebook.setOnClickListener {

        }
    }

    private fun setupButtonContinue(){
        buttonContinue.setOnClickListener {

            activity!!.hideKeyboard(this!!.view!!) // ปิด keyboard

            when {
                editTextMobileNumber.text.length in  0..9 //ถ้า editTextMobileNumber ไม่ครบ 10 ตัว
                -> editTextMobileNumber.error = "You must specify mobile number 10 characters" // แจ้ง error ที่ editTextMobileNumber
                editTextPassword.text.length !in 4..12 -> editTextPassword.error = "You must specify password between 4 - 12 characters"
                else -> callApiSignInForMobileNumber()
            }
        }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
