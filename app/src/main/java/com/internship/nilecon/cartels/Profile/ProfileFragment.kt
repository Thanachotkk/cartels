package com.internship.nilecon.cartels.Profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.auth0.android.jwt.JWT
import com.bumptech.glide.Glide

import com.internship.nilecon.cartels.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_profile.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ProfileFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonImageViewProfile()
        setupButtonMobileNumber()
        setupButtonName()
        setupButtonPassword()
        setUpProfile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    imageViewProfile!!.setImageURI(result.uri)
                }
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }
    private fun setUpProfile(){
        //---------------------------- SharePreferences ----------------------------------------
        val perfs = activity!!.getSharedPreferences(getString(R.string.app_name)/*ตั้งชื่อของ SharedPreferences*/
                , Context.MODE_PRIVATE/*SharedPreferences แบบเห็นได้เฉพาะ app นี้เท่านั้น MODE_PRIVATE*/)
        val token = perfs.getString("Token", null) //ดึงค่า Token ที่เก็บไว้ ใน SharedPreferences
        val nameInHeader = JWT(token).getClaim("Name").asString() //แปลง Token เป็น name
        val mobileNumberInHeader = JWT(token).getClaim("MobileNumber").asString() //แปลง Token เป็น MobileNumber
        val urlPictureInHeader = JWT(token).getClaim("PhotoUrl").asString() //แปลง Token เป็น UrlPicture
        Glide.with(this)
                .load(urlPictureInHeader)
                .into(imageViewProfile)
        textViewNameProfile.text = nameInHeader
        textViewMobileNumber.text = mobileNumberInHeader
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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ProfileFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun setupButtonImageViewProfile() {
        imageViewProfile.setOnClickListener {
            CropImage.activity().setAspectRatio(1, 1).start(this.context!!, this)
        }
    }

    private fun setupButtonPassword() {
        textViewPassword.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right)
                    .replace(R.id.fragmentProfile, ProfilePasswordFragment())
                    .addToBackStack(this.javaClass.name)
                    .commit()
        }
    }

    private fun setupButtonMobileNumber() {
        textViewMobileNumber.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right)
                    .replace(R.id.fragmentProfile, ProfileMobileNumberFragment())
                    .addToBackStack(this.javaClass.name)
                    .commit()
        }
    }

    private fun setupButtonName() {

        textViewNameProfile.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right)
                    .replace(R.id.fragmentProfile, ProfileNameFragment())
                    .addToBackStack(this.javaClass.name)
                    .commit()
        }

    }
}
