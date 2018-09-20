package com.internship.nilecon.cartels.MyParking

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.internship.nilecon.cartels.API.MyParkingList

import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.fragment_my_parking.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [myParkingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [myParkingFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class myParkingFragment : Fragment() ,RecyclerViewMyParkingAdapter.OnItemClickListener{

    // TODO: Rename and change types of parameters
    private var myParkingList : ArrayList<MyParkingList> = ArrayList()
    private var recyclerViewMyParkingAdapter = RecyclerViewMyParkingAdapter()
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun OnItemClick(position: Int) {
        myParkingList.removeAt(position)
        recyclerViewMyParkingAdapter.notifyItemRemoved(position)
        recyclerViewMyParkingAdapter.notifyItemRangeChanged(position,myParkingList.size)
    }

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
        myParkingList.add(MyParkingList("Tower","A","A12","Daily","01/01/2018-05/01/2018","24 Hours","Car"))
        myParkingList.add(MyParkingList("Home", null.toString(),null.toString(),"Daily","01/01/2018-05/01/2018","24 Hours","Car"))

        recyclerViewMyParkingAdapter.setMyParkingList(myParkingList)
        RecyclerViewMyParking.layoutManager = LinearLayoutManager(context)
        recyclerViewMyParkingAdapter.setOnItemClickListener(this)
        RecyclerViewMyParking.adapter = recyclerViewMyParkingAdapter
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
         * @return A new instance of fragment myParkingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                myParkingFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
