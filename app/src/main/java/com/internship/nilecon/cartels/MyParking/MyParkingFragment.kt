package com.internship.nilecon.cartels.MyParking

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.internship.nilecon.cartels.API.Parking

import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.fragment_my_parking.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MyParkingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MyParkingFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MyParkingFragment : Fragment(){

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
        return inflater.inflate(R.layout.fragment_my_parking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViewMyParking()
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
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
         * @return A new instance of fragment MyParkingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MyParkingFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun setupRecyclerViewMyParking(){
        var recyclerViewMyParkingAdapter = RecyclerViewMyParkingAdapter()
        recyclerViewMyParkingAdapter.setOnItemClickListener(object : RecyclerViewMyParkingAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                MaterialDialog.Builder(activity!!)
                        .title("Remove parking")
                        .content("Are you sure you want to delete this parking?")
                        .positiveText("yes")
                        .onPositive { dialog, which ->
                            recyclerViewMyParkingAdapter.removeItem(position)
                        }
                        .negativeText("No")
                        .show()
            }
        })
        recyclerViewMyParkingAdapter.setParkingList(getParkingList())
        recyclerViewMyParking.layoutManager = LinearLayoutManager(context)
        recyclerViewMyParking.adapter = recyclerViewMyParkingAdapter

    }


    private fun getParkingList() : ArrayList<Parking>{
        val parkingList = ArrayList<Parking>()

        parkingList.add(Parking("บ้าน",null,null,"Daily","11/01/2561 - 13/01/2561","24 hour","Car"))
        parkingList.add(Parking("บ้าน",null,null,"Daily","11/01/2561 - 13/01/2561","24 hour","Car"))
        parkingList.add(Parking("บ้าน",null,null,"Daily","11/01/2561 - 13/01/2561","24 hour","Car"))
        parkingList.add(Parking("ตึก","A","A1","Daily","11/01/2561 - 13/01/2561","24 hour","Car"))
        parkingList.add(Parking("ตึก","A","A2","Daily","11/01/2561 - 13/01/2561","24 hour","Car"))
        parkingList.add(Parking("ตึก","A","A3","Daily","11/01/2561 - 13/01/2561","24 hour","Car"))
        return parkingList
    }
}
