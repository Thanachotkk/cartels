package com.internship.nilecon.cartels.SignUp

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.auth0.android.jwt.JWT
import com.internship.nilecon.cartels.API.*

import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_step4.*
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
 * [Step4Fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Step4Fragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Step4Fragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_step4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEditTextPassword()
        setupEditTextConfirmPassword()
        setupButtonNext()
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
         * @return A new instance of fragment Step4Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                Step4Fragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun callApiSignUp() {

        activity!!.relativeLayoutLoading.visibility = View.VISIBLE // เปิด Loading

        mApi = Api().Declaration(activity!!, AuthenticationsInterface::class.java)
                .signUp(UserForSignUpDTO(SIGN_UP.UserForSignUpDTO.MobileNumber
                        ,SIGN_UP.UserForSignUpDTO.Name
                        ,SIGN_UP.UserForSignUpDTO.GoogleId
                        ,SIGN_UP.UserForSignUpDTO.FacebookId
                        ,SIGN_UP.UserForSignUpDTO.Password))

        (mApi as Call<Token>).enqueue(object : Callback<Token>{
            override fun onFailure(call: Call<Token>, t: Throwable) {

            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {

                activity!!.relativeLayoutLoading.visibility = View.GONE // ปิด Loading
                var token = response.body()!!.token //แปลง Token ที่ได้มาให้เป็น String

                var editor = activity!!.getSharedPreferences(getString(R.string.app_name)/*ตั้งชื่อของ SharedPreferences*/
                        ,Context.MODE_PRIVATE/*SharedPreferences แบบเห็นได้เฉพาะ app นี้เท่านั้น MODE_PRIVATE*/)
                        .edit()  // ประกาศใช้ SharedPreferences เพื่อเก็บ Token
                editor.putString("Token",token) /*เก็บ token ลง SharedPreferences โดยอ้างชื่อว่า Token*/
                editor.commit() /*ยืนยันการบันทึก SharedPreferences*/

            }
        })

    }

    private fun  setupEditTextPassword(){
        editTextPassword.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length !in 4..14) editTextPassword.error = "You must specify password between 4 - 12 characters"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun  setupEditTextConfirmPassword(){
        editTextConfirmPassword.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (editTextConfirmPassword.text.toString() != editTextPassword.text.toString()) editTextConfirmPassword.error = "Password and confirm password dose not match"
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

            when {
                editTextPassword.text.length !in 4..14 -> editTextPassword.error = "You must specify password between 4 - 12 characters"
                editTextPassword.text.toString() != editTextConfirmPassword.text.toString() -> editTextConfirmPassword.error = "Password and confirm password dose not match"
                else -> {
                    SIGN_UP.UserForSignUpDTO.Password = editTextConfirmPassword.text.toString()
                    callApiSignUp()


                }
            }
        }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
