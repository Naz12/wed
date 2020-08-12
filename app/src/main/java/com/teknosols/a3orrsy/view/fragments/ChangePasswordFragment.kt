package com.teknosols.a3orrsy.view.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.response.BaseResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.UserResponse
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.gotoGlobalNavigationActivity
import com.teknosols.a3orrsy.other.extensions.obtainViewModel
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.viewmodel.ChangePasswordViewModel
import com.teknosols.a3orrsy.viewmodel.SignInViewModel
import kotlinx.android.synthetic.main.activity_global_navigation.*
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.sign_header.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*


class ChangePasswordFragment : BaseFragment(), View.OnClickListener{

    lateinit var viewModel: ChangePasswordViewModel

    override fun onClick(v: View?) {
        when(v){
            btnChangePassword ->{
                validationChecks()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_change_password, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = obtainViewModel(ChangePasswordViewModel::class.java)

        viewModel.snackbarMessage.observe(viewLifecycleOwner, object : Observer<OneShotEvent<String>> {
            override fun onChanged(t: OneShotEvent<String>?) {
                var msg = t?.getContentIfNotHandled()
                if (!msg.isNullOrEmpty())
                    showAlertDialog(msg)
            }
        })

        viewModel.progressBar.observe(viewLifecycleOwner, object : Observer<OneShotEvent<Boolean>> {
            override fun onChanged(t: OneShotEvent<Boolean>?) {
                var show = t?.getContentIfNotHandled()
                if (show != null)
                    showProgressDialog(show)
            }
        })

        viewModel.baseResponse.observe(viewLifecycleOwner, object : Observer<OneShotEvent<BaseResponse>> {
            override fun onChanged(t: OneShotEvent<BaseResponse>?) {
                var show = t?.getContentIfNotHandled()
                show?.let {
                    (activity as BaseActivity).showAlertDialog(it.message)
                    onBackPress()
                }
            }
        })

        (activity as GlobalNavigationActivity).toolbarLayout.header_textview.text = "Change Password"
        rlTopBar.visibility = View.GONE
        btnChangePassword.setOnClickListener(this)
    }

    fun validationChecks(){
        if(etOldPassword.text.isNullOrEmpty()){
            etOldPassword.requestFocus()
            etOldPassword.error = "Enter OLd Password"
        }else if(etNewPassword.text.isNullOrEmpty()){
            etNewPassword.requestFocus()
            etNewPassword.error = "Enter OLd Password"
        }else if(etNewPassword.text.length < 6){
            etNewPassword.error = "Enter Minimum 6 Characters"
            etNewPassword.requestFocus()
        }else if(etConfirmPassword.text.isNullOrEmpty()){
            etConfirmPassword.requestFocus()
            etConfirmPassword.error = "Enter OLd Password"
        }else if(etConfirmPassword.text.toString() != etNewPassword.text.toString()){
            etConfirmPassword.requestFocus()
            etConfirmPassword.error = "Password Does Not March"
        }else{
            viewModel.changePassword(sessionManager.getUserId(), etOldPassword.text.toString(), etNewPassword.text.toString())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChangePasswordFragment()
    }

}
