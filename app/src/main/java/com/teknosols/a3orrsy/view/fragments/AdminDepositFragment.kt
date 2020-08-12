package com.teknosols.a3orrsy.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teknosols.a3orrsy.R
import kotlinx.android.synthetic.main.fragment_admin_deposit.*
import kotlinx.android.synthetic.main.sign_header.*


class AdminDepositFragment : BaseFragment(), View.OnClickListener{

    lateinit var mListener: OnUpdateDeposit

    override fun onClick(v: View?) {
        when(v){
            btnBack ->{
                onBackPress()
            }
            btnSubmit ->{
                mListener.onUpdate(etDeposit.text.toString())
                onBackPress()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_admin_deposit, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvHeader.text = getString(R.string.deposit_)
        btnBack.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
    }

    interface OnUpdateDeposit{
        fun onUpdate(amount: String)
    }

    companion object {
        @JvmStatic
        fun newInstance( mListener: OnUpdateDeposit): AdminDepositFragment{
            var fragment = AdminDepositFragment()
            var bundle = Bundle()
            fragment.mListener = mListener
            fragment.arguments = bundle
            return fragment

        }
    }

}
