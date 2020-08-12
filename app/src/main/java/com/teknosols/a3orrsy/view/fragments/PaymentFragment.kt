package com.teknosols.a3orrsy.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import kotlinx.android.synthetic.main.fragment_payment.*


class PaymentFragment : BaseFragment(), View.OnClickListener{


    override fun onClick(v: View?) {
        when(v){
            tvCash ->{
                clearStack()
                (activity as GlobalNavigationActivity).showAlertDialog(getString(R.string.thanks_for_confirmation))
//                Toast.makeText(context!!, "Under Development", Toast.LENGTH_SHORT).show()
            }
            tvBank ->{
                Toast.makeText(context!!, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
            }
            tvCreditCard ->{
                Toast.makeText(context!!, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_payment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvCash.setOnClickListener(this)
        tvBank.setOnClickListener(this)
        tvCreditCard.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearStack()

    }
    

    companion object {
        @JvmStatic
        fun newInstance() = PaymentFragment()
    }


}
