package com.internship.nilecon.cartels.SignUp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.internship.nilecon.cartels.API.Api
import com.internship.nilecon.cartels.API.AuthenticationsInterface
import com.internship.nilecon.cartels.API.UserForIsUserExistsBySocialDTO

import com.internship.nilecon.cartels.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_step3.*
import okhttp3.MediaType
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val GOOGLE_SIGN_IN_REQUEST_CODE = 9001

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Step3Fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Step3Fragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Step3Fragment : Fragment(), GoogleApiClient.OnConnectionFailedListener {



    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mApi : Any? = null
    private val mCallbackFacebook = CallbackManager.Factory.create()
    private var mGoogleApiClient: GoogleApiClient? = null
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

    override fun onConnectionFailed(p0: ConnectionResult) {
        print(p0.errorMessage)
    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient!!.stopAutoManage(activity!!)
        mGoogleApiClient!!.disconnect()
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
        mCallbackFacebook.onActivityResult(requestCode, resultCode, data)
            if(resultCode == Activity.RESULT_OK){
                when (requestCode){
                    CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                        val result = CropImage.getActivityResult(data)
                        SIGN_UP.UserForAddOrReplacePhotoDTO.Photo = result.bitmap
                        imageViewProfile!!.setImageURI(result.uri)
                    }
                    GOOGLE_SIGN_IN_REQUEST_CODE  -> {
                        val googleResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

                        when(googleResult.isSuccess){
                            true -> {
                                callApiIsUserExistsBySocial(
                                        UserForIsUserExistsBySocialDTO(
                                                googleResult.signInAccount!!.id,
                                                "Google"),
                                        googleResult
                                )
                            }
                            else -> {

                            }
                        }

                    }
                }
            }


    }

    private fun callApiIsUserExistsBySocial(userForIsUserExistsBySocialDTO: UserForIsUserExistsBySocialDTO,userFromSocial: Any){
        activity!!.constraintLayoutLayoutLoading.visibility = View.VISIBLE // เปิด Loading
        SIGN_UP.UserForSignUpDTO.FacebookId = null
        SIGN_UP.UserForSignUpDTO.GoogleId = null

        mApi = Api().Declaration(activity!!, AuthenticationsInterface::class.java)
                .isUserExistsBySocial(userForIsUserExistsBySocialDTO) //ตั้งค่า Api request

        (mApi as Call<Void>).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                activity!!.constraintLayoutLayoutLoading.visibility = View.GONE //ปิด Loading
                print(t.message)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                activity!!.constraintLayoutLayoutLoading.visibility = View.GONE //ปิด Loading

                when(response.code()){
                    200 ->{
                        when(userForIsUserExistsBySocialDTO.SocialType){
                            "Facebook" -> {
                                SIGN_UP.UserForSignUpDTO.FacebookId = (userFromSocial as Profile).id
                                SIGN_UP.UserForSignUpDTO.Name = userFromSocial.name
                                LoginManager.getInstance().logOut()
                                activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
                                        R.anim.enter_from_right,
                                        R.anim.exit_to_left,
                                        R.anim.enter_from_left,
                                        R.anim.exit_to_right)
                                        .replace(R.id.fragmentSignUp,Step4Fragment())
                                        .addToBackStack(this.javaClass.name)
                                        .commit()
                            }
                            "Google" -> {
                                SIGN_UP.UserForSignUpDTO.GoogleId = (userFromSocial as GoogleSignInResult).signInAccount!!.id
                                SIGN_UP.UserForSignUpDTO.Name = userFromSocial.signInAccount!!.displayName
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
                    }
                    400 ->{
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
        })  //ส่งคำร้องขอ Api request ไปที่ Server
    }

    private fun setupButtonNext(){
        buttonNext.setOnClickListener {

            activity!!.hideKeyboard(this!!.view!!) // ปิด keyboard

            if(editTextName.text.isEmpty()) //ถ้า editTextName ไม่มีการกรอกค่า
                editTextName.error = "You must specify name or connect with google or facebook" // แจ้ง error ที่ editTextName
            else {
                SIGN_UP.UserForSignUpDTO.FacebookId = null
                SIGN_UP.UserForSignUpDTO.GoogleId = null
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

        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(context!!).enableAutoManage(activity!!, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build()

        buttonGoogle.setOnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST_CODE)
            when(mGoogleApiClient!!.isConnected){true -> Auth.GoogleSignInApi.signOut(mGoogleApiClient)}
        }
    }

    private fun setupButtonFacebook(){
        buttonFacebook.setOnClickListener {
            buttonFacebookMain.performClick()
        }
        buttonFacebookMain.setReadPermissions(Arrays.asList("user_photos", "email", "public_profile"))
        buttonFacebookMain.fragment = this
        buttonFacebookMain.registerCallback(mCallbackFacebook, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                val parameters = Bundle()
                parameters.putString("fields", "id,name,link,email,picture")
                val request = GraphRequest.newMeRequest(result?.accessToken) { jsonObject, _ ->
                    callApiIsUserExistsBySocial(UserForIsUserExistsBySocialDTO(Profile.getCurrentProfile().id,"Facebook"),Profile.getCurrentProfile())
                }
                request.parameters = parameters
                request.executeAsync()

            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {

            }

        })

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
