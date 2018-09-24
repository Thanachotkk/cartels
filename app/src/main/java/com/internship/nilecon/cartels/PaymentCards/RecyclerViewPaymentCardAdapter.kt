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

    private var paymentCardList= ArrayList<PaymentCard>()
    private var listener : OnItemClickListener? = null

    fun setPaymentCardList(paymentCardList :ArrayList<PaymentCard>) {
        this.paymentCardList = paymentCardList
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.listener = onItemClickListener
    }

    fun removeItem(position: Int){
        paymentCardList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
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
        holder.itemView.buttonDelete.setOnClickListener {
            listener!!.onRemoveItemClick(position)
        }
        holder.itemView.setOnClickListener {
            listener!!.onItemClick(paymentCardList!![position])
        }
    }

    interface OnItemClickListener{
        fun onRemoveItemClick(position: Int)
        fun onItemClick(paymentCard: PaymentCard)
    }
}

class PaymentCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


