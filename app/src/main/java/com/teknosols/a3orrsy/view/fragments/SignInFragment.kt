package com.teknosols.a3orrsy.view.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.UserResponse
import com.teknosols.a3orrsy.other.extensions.*
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.viewmodel.SignInViewModel
import com.teknosols.a3orrsy.viewmodel.SignupViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.etPassword
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.sign_header.*
import android.app.Activity
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import android.R.id.edit
import android.content.DialogInterface
import android.app.AlertDialog
import android.content.res.Configuration
import com.teknosols.a3orrsy.view.activites.LandingActivity
import java.util.*


class SignInFragment : BaseFragment(), View.OnClickListener{

    lateinit var viewModel: SignInViewModel
    lateinit var token: String

    override fun onClick(v: View?) {
        when(v){
            btnSignIn ->{
                validaionChecks()
            }
            tvForgotPassword ->{
                val forgotPasswordFragment = ForgotPasswordFragment.newInstance()
                (activity as BaseActivity).replaceFragment(forgotPasswordFragment,forgotPasswordFragment.getSimpleName(),true,false)
            }
            tvSignUp ->{
                val signUpFragment = SignUpFragment.newInstance(token)
                (activity as BaseActivity).replaceFragment(signUpFragment,signUpFragment.getSimpleName(),true,false)

            }
            btnChangeLanguage ->{
                (activity as LandingActivity).showChangeLanguageDialog()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            this.token = it!!["token"] as String
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvHeader.text = getString(R.string.sign_in)
        btnBack.visibility = View.GONE
        btnChangeLanguage.setOnClickListener(this)

        viewModelHandler()
        onClickListerners()

    }

    fun validaionChecks(){
        if(etPhoneNoSignIn.text.isNullOrEmpty()){
            etPhoneNoSignIn.error = getString(R.string.enter_phone_no)
            etPhoneNoSignIn.requestFocus()
        }else if(etPassword.text.isNullOrEmpty()){
            etPassword.error = "Enter Password"
            etPassword.requestFocus()
        }else if(etPassword.text.length < 6){
            etPassword.error = "Enter Minimum 6 Characters"
            etPassword.requestFocus()
        }else{
            viewModel.login(ccpSignIn.selectedCountryCodeWithPlus+etPhoneNoSignIn.text.toString(), etPassword.text.toString(), token)
        }
    }

    fun viewModelHandler(){
        viewModel = obtainViewModel(SignInViewModel::class.java)

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

        viewModel.user.observe(viewLifecycleOwner, object : Observer<OneShotEvent<UserResponse>> {
            override fun onChanged(t: OneShotEvent<UserResponse>?) {
                var show = t?.getContentIfNotHandled()
                show?.let {
                    sessionManager.setUser(it.data)
                    var loginAs = sessionManager.getLoginAs()
                    when(loginAs){
                        "admin" ->{
                            activity?.gotoAdminActivity()
                        }
                        "customer" ->{
                            activity?.gotoGlobalNavigationActivity()
                        }
                        "manager" ->{

                        }
                        else -> {
                            showAlertDialog("No login_as status found!!")
                        }
                    }
                }
            }
        })
    }

    fun onClickListerners(){
        btnSignIn.setOnClickListener(this)
        tvForgotPassword.setOnClickListener(this)
        tvSignUp.setOnClickListener(this)
    }

    companion object {

        @JvmStatic
        fun newInstance(token: String): SignInFragment {
            val fragment = SignInFragment()
            val bundle = Bundle()
            bundle.putSerializable("token", token)
            fragment.arguments = bundle
            return fragment
        }
    }


}
