package com.teknosols.a3orrsy.view.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.response.BaseResponse
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.obtainViewModel
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.activites.LandingActivity
import com.teknosols.a3orrsy.viewmodel.ChangePasswordViewModel
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.sign_header.*
import java.util.concurrent.TimeUnit


class ForgotPasswordFragment : BaseFragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v) {
            btnBack -> {
                onBackPress()
            }
            btnSubmit -> {
                validationChecks()
            }
            btnChangeLanguage -> {
                (activity as LandingActivity).showChangeLanguageDialog()
            }
            btnVerify -> {
                verifyVerificationCode(
                    mVerificationId.toString(),
                    etVerificationCode.text.toString()
                )
            }
            btnResetPassword -> {
                validationChecksForPassword()
            }
            tvResendForgotPassword -> {
                llCounterForgotPassword.visibility = View.VISIBLE
                llResendCodeForgotPassword.setVisibility(View.GONE)
                sendVerificationCode()
            }
        }
    }


    lateinit var viewModel: ChangePasswordViewModel
    var mVerificationId: String? = null
    lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken
    var cTimer: CountDownTimer? = null
    var mintsOfTimer: Long = 0
    lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        tvHeader.text = getString(R.string.forget_password_)
        btnChangeLanguage.setOnClickListener(this)

        viewModel = obtainViewModel(ChangePasswordViewModel::class.java)

        viewModel.snackbarMessage.observe(
            viewLifecycleOwner,
            object : Observer<OneShotEvent<String>> {
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

        viewModel.baseResponse.observe(
            viewLifecycleOwner,
            object : Observer<OneShotEvent<BaseResponse>> {
                override fun onChanged(t: OneShotEvent<BaseResponse>?) {
                    var show = t?.getContentIfNotHandled()
                    show?.let {
                        (activity as BaseActivity).showAlertDialog(it.message)
                        onBackPress()
                    }
                }
            })

        btnBack.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
        btnVerify.setOnClickListener(this)
        btnResetPassword.setOnClickListener(this)
        tvResendForgotPassword.setOnClickListener(this)
    }

    private fun validationChecks() {

        if (etPhoneNoForgotPassword.text.isNullOrEmpty()) {
            etPhoneNoForgotPassword.requestFocus()
            etPhoneNoForgotPassword.error = getString(R.string.enter_phone_no)
        } else {
            llProgressBarForgotPass.visibility = View.VISIBLE
            sendVerificationCode()
        }
    }

    private fun sendVerificationCode() {
        llProgressBarForgotPass.visibility = View.VISIBLE
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            ccpForgotPass.selectedCountryCodeWithPlus + etPhoneNoForgotPassword.text.toString(), // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            activity!!, // Activity (for callback binding)
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                    Log.d("PhoneAuthentication", "onVerificationCompleted:" + credential.smsCode!!)
                    llProgressBarForgotPass.visibility = View.GONE
                }

                override fun onVerificationFailed(e: FirebaseException) {

                    llProgressBarForgotPass.visibility = View.GONE

                    Log.w("Phone Authentication", "onVerificationFailed", e)

                    if (e is FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        // ...
                        Log.w("PhoneAuthentication", "FirebaseAuthInvalidCredentialsException ", e)
                    } else if (e is FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        // ...
                        Log.w("PhoneAuthentication", "FirebaseTooManyRequestsException ", e)

                    }
                }

                override fun onCodeSent(
                    verificationId: String?,
                    token: PhoneAuthProvider.ForceResendingToken?
                ) {
                    Log.d("PhoneAuthentication", "onCodeSent:" + verificationId!!)
                    mVerificationId = verificationId
                    mResendToken = token!!
                    llProgressBarForgotPass.visibility = View.GONE
                    llEnterPhoneNumber.visibility = View.GONE
                    llVerificationCode.visibility = View.VISIBLE
                    timerSettings()
                    startTimer()
                }

                override fun onCodeAutoRetrievalTimeOut(s: String?) {
                    super.onCodeAutoRetrievalTimeOut(s)
                }
            })
    }

    private fun verifyVerificationCode(verificationId: String, code: String) {

        llProgressBarForgotPass.visibility = View.VISIBLE
        try {
            Log.i("mVerificationId", mVerificationId)
            //checking the code is correct or not
            if (!mVerificationId.isNullOrEmpty()) {
                val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)
                signInWithPhoneAuthCredential(credential)
            }
        } catch (e: Exception) {
            Log.e("ConfirmationActivity", "VerifyVerificationCode: $e")
            llProgressBarForgotPass.visibility = View.GONE
            showAlertDialog(getString(R.string.firebase_msg))
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!,
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        llProgressBarForgotPass.visibility = View.GONE
                        llVerificationCode.visibility = View.GONE
                        llNewPassword.visibility = View.VISIBLE
                    } else {

                        var message = "Somthing is wrong, we will fix it soon..."

                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered..."
                            Log.w("Phone Authentication", "onVerificationFailed")
                            showAlertDialog(message)
                            llProgressBarForgotPass.visibility = View.GONE

                        }
                    }
                })
    }

    internal fun timerSettings() {
        val oneMint: Long = 61000
        mintsOfTimer = oneMint
    }

    internal fun startTimer() {
        cTimer = object : CountDownTimer(mintsOfTimer, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000

                val str = (String.format("%02d", seconds / 60)
                        + ":" + String.format("%02d", seconds % 60))

                tvCounterForgotPassword.setText("Resend in $str sec")

                if (str == "00:01") {
                    val handler = Handler()
                    handler.postDelayed({
                        tvCounterForgotPassword.setText("Resend in 00:00 sec")
                        llResendCodeForgotPassword.setVisibility(View.VISIBLE)
                        llCounterForgotPassword.setVisibility(View.GONE)
                        showAlertDialog(getString(R.string.time_out))
                    }, 1000)

                }
            }

            override fun onFinish() {
                //                binding.llResendCode.setVisibility(View.VISIBLE);
                //                binding.llCounter.setVisibility(View.GONE);
//                Toast.makeText(context, getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                //                finish();
            }
        }.start()

    }

    //cancel timer
    internal fun cancelTimer() {
        if (cTimer != null)
            cTimer!!.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
        cancelTimer()
    }

    private fun validationChecksForPassword() {

        if (etNewPasswordForgotPass.text.isNullOrEmpty()) {
            etNewPasswordForgotPass.error = "Enter Password"
            etNewPasswordForgotPass.requestFocus()
        } else if (etNewPasswordForgotPass.text.length < 6) {
            etNewPasswordForgotPass.error = "Enter Minimum 6 Characters"
            etNewPasswordForgotPass.requestFocus()
        } else if (etConfirmPasswordForgotPass.text.isNullOrEmpty()) {
            etConfirmPasswordForgotPass.error = "Enter Password"
            etConfirmPasswordForgotPass.requestFocus()
        } else if (etConfirmPasswordForgotPass.text.length < 6) {
            etConfirmPasswordForgotPass.error = "Enter Minimum 6 Characters"
            etConfirmPasswordForgotPass.requestFocus()
        } else if (!etNewPasswordForgotPass.text.toString().equals(etConfirmPasswordForgotPass.text.toString())) {
            etConfirmPasswordForgotPass.error = "Both Passwords Not Match"
            etConfirmPasswordForgotPass.requestFocus()
        } else {
            viewModel.userForgotPassword(ccpForgotPass.selectedCountryCodeWithPlus + etPhoneNoForgotPassword.text.toString() , etNewPasswordForgotPass.text.toString())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ForgotPasswordFragment()
    }

}
