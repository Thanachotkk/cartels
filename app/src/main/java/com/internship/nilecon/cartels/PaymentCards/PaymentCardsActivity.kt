package com.internship.nilecon.cartels.PaymentCards

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_payment_cards.*

class PaymentCardsActivity : AppCompatActivity()
        , PaymentCardsFragment.OnFragmentInteractionListener
        , AddCardFragment.OnFragmentInteractionListener{

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_cards)

        setupButtonBack()
        setupButtonAddCard()
        setupPaymentCardsFragment()
    }

    private fun setupPaymentCardsFragment(){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentPaymentCards, PaymentCardsFragment())
                .commit()

        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragmentPaymentCards).javaClass.simpleName
            when (fragment) {
                PaymentCardsFragment().javaClass.simpleName -> {
                    textViewActionBar.text = "Payment Cards"
                    buttonAdd.visibility = View.VISIBLE
                }
                AddCardFragment().javaClass.simpleName -> {
                    textViewActionBar.text = "Add Card"
                    buttonAdd.visibility = View.GONE
                }
            }
        }
    }

    private fun setupButtonBack(){
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupButtonAddCard(){
        buttonAdd.setOnClickListener {
            supportFragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right)
                    .replace(R.id.fragmentPaymentCards, AddCardFragment())
                    .addToBackStack(this.javaClass.name)
                    .commit()
        }
    }
}
