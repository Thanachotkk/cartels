package com.internship.nilecon.cartels.PaymentCards

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.internship.nilecon.cartels.API.PaymentCard
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.fragment_add_card.view.*
import kotlinx.android.synthetic.main.view_recycler_view_item_payment_cards.view.*

class RecyclerViewPaymentCardAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var paymentCardList: List<PaymentCard>? = null

    fun setPaymentCardList(paymentCardList: List<PaymentCard>?) {
        this.paymentCardList = paymentCardList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recycler_view_item_payment_cards, parent, false)
        return PaymentCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return paymentCardList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.textViewCardNunber4.text = paymentCardList!![position].cardNumber
        holder.itemView.textViewCardholderNameValue.text = paymentCardList!![position].name
        holder.itemView.textViewExpireDateValue.text = paymentCardList!![position].expiry
    }
}

class PaymentCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


