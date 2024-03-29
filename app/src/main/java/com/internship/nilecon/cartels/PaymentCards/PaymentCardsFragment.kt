package com.internship.nilecon.cartels.PaymentCards

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.internship.nilecon.cartels.API.PaymentCard
import com.internship.nilecon.cartels.API.Token

import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.fragment_payment_cards.*
import java.lang.reflect.Array
import java.text.FieldPosition

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PaymentCardsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PaymentCardsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PaymentCardsFragment : Fragment(){


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
        return inflater.inflate(R.layout.fragment_payment_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewPaymentCard()
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
         * @return A new instance of fragment PaymentCardsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                PaymentCardsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }




    private fun setupRecyclerViewPaymentCard(){
        var recyclerViewPaymentCardAdapter = RecyclerViewPaymentCardAdapter()
        recyclerViewPaymentCardAdapter.setOnItemClickListener(object : RecyclerViewPaymentCardAdapter.OnItemClickListener{

            override fun onItemClick(paymentCard: PaymentCard) {
                when (activity!!.callingActivity) {
                    null -> {

                    }
                    else -> {
                        var intent = activity!!.intent
                        intent.putExtra("paymentCard",paymentCard)
                        activity!!.setResult(Activity.RESULT_OK,intent)
                        activity!!.finish()
                    }
                }
            }

            override fun onRemoveItemClick(position: Int) {
                MaterialDialog.Builder(activity!!)
                        .title("Remove credit card")
                        .content("Are you sure you want to delete this card?")
                        .positiveText("yes")
                        .onPositive { dialog, which ->
                            recyclerViewPaymentCardAdapter.removeItem(position)
                        }
                        .negativeText("No")
                        .show()
            }
        })
        recyclerViewPaymentCardAdapter.setPaymentCardList(getPaymentCardList())
        recyclerViewPaymentCard.layoutManager = LinearLayoutManager(context)
        recyclerViewPaymentCard.adapter= recyclerViewPaymentCardAdapter
    }

    private fun getPaymentCardList() : ArrayList<PaymentCard>{
        val paymentCardList = ArrayList<PaymentCard>()
        paymentCardList.add(PaymentCard(
                "0000",
                "kaveepart",
                "123",
                "01/20"
        ))
        paymentCardList.add(PaymentCard(
                "1111",
                "kaveepart",
                "123",
                "01/20"
        ))
        paymentCardList.add(PaymentCard(
                "2222",
                "kaveepart",
                "123",
                "01/20"
        ))
        paymentCardList.add(PaymentCard(
                "3333",
                "kaveepart",
                "123",
                "01/20"
        ))
        paymentCardList.add(PaymentCard(
                "4444",
                "kaveepart",
                "123",
                "01/20"
        ))
        paymentCardList.add(PaymentCard(
                "5555",
                "kaveepart",
                "123",
                "01/20"
        ))
        return paymentCardList
    }
}
