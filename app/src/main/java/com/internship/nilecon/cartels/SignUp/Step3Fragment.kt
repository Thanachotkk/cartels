package com.internship.nilecon.cartels.SignUp

import android.app.Activity
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.util.WorkSourceUtil.TAG
import com.google.android.gms.tasks.Task
import com.internship.nilecon.cartels.API.*
import com.internship.nilecon.cartels.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_step3.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedOutputStream
import java.io.File
import java.util.*

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
class Step3Fragment : Fragment(), GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(context, "Fail Connection", Toast.LENGTH_SHORT).show()
    }

    var bitmapresult: Bitmap? = null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mApi: Any? = null
    private var listener: OnFragmentInteractionListener? = null
    lateinit var GoogleID: String
    val callbackFB = CallbackManager.Factory.create()
    private var googleApiClient: GoogleApiClient? = null


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
        if (mApi != null) { // ถ้า Api request ยังไม่สำเร็จ
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
        var RC_SIGN_IN = 9001
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
        super.onActivityResult(requestCode, resultCode, data)
        callbackFB.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val googleresult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(googleresult)
        }
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    imageViewProfile!!.setImageURI(result.uri)

                }
            }
        }
    }


    private fun setupButtonNext() {
        buttonNext.setOnClickListener {

            activity!!.hideKeyboard(this!!.view!!) // ปิด keyboard

            if (editTextName.text.isEmpty()) //ถ้า editTextName ไม่มีการกรอกค่า
                editTextName.error = "You must specify name or connect with google or facebook" // แจ้ง error ที่ editTextName
            else {
                ObjectAPi.SocialType = "Nosocial"
                ObjectAPi.Name = editTextName.text.toString()
                activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right)
                        .replace(R.id.fragmentSignUp, Step4Fragment())
                        .addToBackStack(this.javaClass.name)
                        .commit()
            } //ไป Step4Fragment
        }
    }

    private fun setupButtonGoogle() {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(context!!).enableAutoManage(activity!!, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build()
        buttonGoogle.setOnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(intent, RC_SIGN_IN)
        }

    }

    private fun setupButtonFacebook() {
        buttonFacebook.setOnClickListener {
            buttonfacemain.performClick()
        }
        buttonfacemain.setReadPermissions(Arrays.asList("user_photos", "email", "public_profile"))
        buttonfacemain.fragment = this
        buttonfacemain.registerCallback(callbackFB, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                val accessToken = result!!.accessToken
                val parameters = Bundle()
                parameters.putString("fields", "id,name,link,email,picture")
                val request = GraphRequest.newMeRequest(result?.accessToken) { jsonObject, _ ->
                    ObjectAPi.Name = Profile.getCurrentProfile().name
                    val IDfacebook = Profile.getCurrentProfile().id
                    Log.i("ID", IDfacebook)
                    ObjectAPi.SocialType = "Facebook"
                    ObjectAPi.FacebookID = IDfacebook
                    Log.i("Name", "$IDfacebook")
                    Toast.makeText(activity!!, "$IDfacebook", Toast.LENGTH_SHORT).show()
                    ApiCheck(UserForIsUserExistsBySocialDTO(IDfacebook.toString(), "Facebook"))
                }
                request.parameters = parameters
                request.executeAsync()

            }

            override fun onCancel() {
                Toast.makeText(activity!!, "Cancel Login with Facebook", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(activity!!, "Error : $error", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun setupImageViewProfile() {
        imageViewProfile.setOnClickListener {
            CropImage.activity().setAspectRatio(1, 1).start(this.context!!, this)
        }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun ApiCheck(dataClassSocial: UserForIsUserExistsBySocialDTO) {
        mApi = Api().Declaration(activity!!, AuthenticationsInterface::class.java)
                .SignUpWithSocial(dataClassSocial)
        (mApi as Call<Void>).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                Toast.makeText(activity!!, "Fail", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                if (response!!.code() == 200) {
                    NextStep()
                } else {
                    Log.i("Response", response.toString())
                }
            }
        })
    }

    private fun NextStep() {
        activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right)
                .replace(R.id.fragmentSignUp, Step4Fragment())
                .addToBackStack(this.javaClass.name)
                .commit()
    }

    override fun onPause() {
        super.onPause()
        googleApiClient!!.stopAutoManage(activity!!)
        googleApiClient!!.disconnect()
    }

    private fun handleSignInResult(result: GoogleSignInResult?) {
        if (result!!.isSuccess) {
            val IDGoogle = result.signInAccount!!.id
            val acct = GoogleSignIn.getLastSignedInAccount(activity)
            ObjectAPi.Name = result.signInAccount!!.displayName
            ObjectAPi.GoogleID = IDGoogle!!
            ObjectAPi.SocialType = "Google"
            NextStep()
        } else {
            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
        }
    }
}
