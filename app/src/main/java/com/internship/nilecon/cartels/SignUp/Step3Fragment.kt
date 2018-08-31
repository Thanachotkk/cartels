package com.internship.nilecon.cartels.SignUp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

import com.internship.nilecon.cartels.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_step3.*
import retrofit2.Call

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Step3Fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Step3Fragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Step3Fragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_step3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonNext()
        setupButtonGoogle()
        setupButtonFacebook()
        setupImageViewProfile()
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
         * @return A new instance of fragment Step3Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                Step3Fragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode,  resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when (requestCode){
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    SIGN_UP.UserForAddOrReplacePhotoDTO.Photo = result.bitmap
                    imageViewProfile!!.setImageURI(result.uri)
                }
            }
        }
    }


    private fun setupButtonNext(){
        buttonNext.setOnClickListener {

            activity!!.hideKeyboard(this!!.view!!) // ปิด keyboard

            if(editTextName.text.isEmpty()) //ถ้า editTextName ไม่มีการกรอกค่า
                editTextName.error = "You must specify name or connect with google or facebook" // แจ้ง error ที่ editTextName
            else {
                SIGN_UP.UserForSignUpDTO.Name = editTextName.text.toString()
                activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right)
                    .replace(R.id.fragmentSignUp,Step4Fragment())
                    .addToBackStack(this.javaClass.name)
                    .commit()} //ไป Step4Fragment
        }
    }

    private fun setupButtonGoogle(){
        buttonGoogle.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right)
                    .replace(R.id.fragmentSignUp,Step4Fragment())
                    .addToBackStack(this.javaClass.name)
                    .commit()
        }
    }

    private fun setupButtonFacebook(){
        buttonFacebook.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right)
                    .replace(R.id.fragmentSignUp,Step4Fragment())
                    .addToBackStack(this.javaClass.name)
                    .commit()
        }
    }

    private fun setupImageViewProfile(){
        imageViewProfile.setOnClickListener {
            CropImage.activity().setAspectRatio(1,1).start(this.context!!,this)
        }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }



}
