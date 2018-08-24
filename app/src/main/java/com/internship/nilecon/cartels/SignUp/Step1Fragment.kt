package com.internship.nilecon.cartels.SignUp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.internship.nilecon.cartels.API.Api
import com.internship.nilecon.cartels.API.AuthenticationsInterface
import com.internship.nilecon.cartels.API.UserForSentOtpSmsForSignUpDTO

import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.fragment_step1.*
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
 * [Step1Fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Step1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Step1Fragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_step1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonNext()
    }

    // TODO: Rename method, update argument and hook method into UI event
    private fun onFragmentInteraction(uri: Uri) {
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
         * @return A new instance of fragment Step1Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                Step1Fragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
    private fun setupButtonNext(){

        buttonNext.setOnClickListener {
            callApiSentOptSmsForSignUp()
        }
    }

    private fun callApiSentOptSmsForSignUp(){
        mApi = Api().Declaration(activity!!, AuthenticationsInterface::class.java)
                .sentOtpSmsForSignUp(UserForSentOtpSmsForSignUpDTO(editTextMobileNumber.text.toString()))  //ตั้งค่า Api request

        (mApi as Call<Void>).enqueue(object : Callback<Void>{  //ส่งคำร้องขอ Api request ไปที่ Server

            override fun onFailure(call: Call<Void>, t: Throwable) { //เมื่อ Server ตอบกลับแบบล้มเหลว
                TODO("not implemented")
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) { //เมื่อ Server ตอบกลับแบบสำเร็จ

                when(response.code()){ //ตรวจ status code

                    200 -> { //เมื่อ status code : 200 (Ok)

                        activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
                                R.anim.enter_from_right,
                                R.anim.exit_to_left,
                                R.anim.enter_from_left,
                                R.anim.exit_to_right)
                                .replace(R.id.fragmentSignUp,Step2Fragment())
                                .addToBackStack(this.javaClass.name)
                                .commit() // ไป Step2Fragment
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
                }
            }
        })
    }
}
