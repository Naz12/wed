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
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.UserResponse
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.gotoAdminActivity
import com.teknosols.a3orrsy.other.extensions.gotoGlobalNavigationActivity
import com.teknosols.a3orrsy.other.extensions.obtainViewModel
import com.teknosols.a3orrsy.viewmodel.SignupViewModel
import kotlinx.android.synthetic.main.fragment_phone_varification.*
import kotlinx.android.synthetic.main.sign_header.*
import java.util.concurrent.TimeUnit


class PhoneVerificationFragment : BaseFragment(), View.OnClickListener {

    lateinit var mAuth: FirebaseAuth
    var mVerificationId: String? = null
    lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken

     var cTimer: CountDownTimer? = null
     var mintsOfTimer: Long = 0

    lateinit var viewModel: SignupViewModel

    lateinit var name: String
    lateinit var phone: String
    lateinit var email: String
    lateinit var password: String
    lateinit var birthday: String
    lateinit var address: String
    lateinit var city: String
    lateinit var country: String
    lateinit var avatar: String
    lateinit var token: String

    override fun onClick(v: View?) {
        when (v) {
            btVerify -> {
                llProgress_bar.visibility = View.VISIBLE
                btVerify.visibility = View.GONE
                verifyVerificationCode(etCode.text.toString())
            }
            tvResend ->{
                llCounter.setVisibility(View.VISIBLE)
                llResendCode.setVisibility(View.GONE)
                timerSettings()
                startTimer()
                phoneAuthentication()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_phone_varification, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        onClickListeners()
        tvHeader?.text = "Verification"
        tvNumber.text = phone
        timerSettings()
        startTimer()
        phoneAuthentication()

        viewModelHandler()

    }

     fun phoneAuthentication() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phone, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            activity!!, // Activity (for callback binding)
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                    Log.d("PhoneAuthentication", "onVerificationCompleted:" + credential.smsCode!!)

                }

                override fun onVerificationFailed(e: FirebaseException) {
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
                }

                override fun onCodeAutoRetrievalTimeOut(s: String?) {
                    super.onCodeAutoRetrievalTimeOut(s)
                }
            })
    }

    private fun verifyVerificationCode(code: String) {

        try {
            Log.i("mVerificationId", mVerificationId)
            //creating the credential
            if (!mVerificationId.isNullOrEmpty()) {
                val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)
                //signing the user
                signInWithPhoneAuthCredential(credential)
            }
        } catch (e: Exception) {
            Log.e("ConfirmationActivity", "VerifyVerificationCode: $e")
            showAlertDialog(getString(R.string.firebase_msg))
            llProgress_bar.visibility = View.GONE
            btVerify.visibility = View.VISIBLE
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!,
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
//                        Toast.makeText(context, "isSuccessful", Toast.LENGTH_SHORT).show()
                        llProgress_bar.visibility = View.GONE
                        btVerify.visibility = View.VISIBLE
                        onVerify(true)

                    } else {

                        var message = "Somthing is wrong, we will fix it soon..."

                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered..."
                            Log.w("Phone Authentication", "onVerificationFailed")
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            showAlertDialog(message)
                            llProgress_bar.visibility = View.GONE
                            btVerify.visibility = View.VISIBLE

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

                tvCounter.setText("Resend in $str sec")

                if (str == "00:01") {
                    val handler = Handler()
                    handler.postDelayed({
                        tvCounter.setText("Resend in 00:00 sec")
                        llResendCode.setVisibility(View.VISIBLE)
                        llCounter.setVisibility(View.GONE)
                        showAlertDialog(getString(R.string.time_out))
                    }, 1000)

                }
            }

            override fun onFinish() {
                //                binding.llResendCode.setVisibility(View.VISIBLE);
                //                binding.llCounter.setVisibility(View.GONE);
//                Toast.makeText(context, getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
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

    fun onClickListeners() {
        btVerify.setOnClickListener(this)
        tvResend.setOnClickListener(this)
    }


    fun viewModelHandler(){
        viewModel = obtainViewModel(SignupViewModel::class.java)

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
                show?.let{
                    sessionManager.setUser(it.data)
                    when(it.data.login_as){
                        "admin" ->{
                            activity?.gotoAdminActivity()
                        }
                        "customer" ->{
                            activity?.gotoGlobalNavigationActivity()
                        }
                        "manager" ->{

                        }
                        else -> {
                            showAlertDialog(getString(R.string.no_login_status))
                        }
                    }
                }
            }
        })

    }

    fun onVerify(isVerified: Boolean){
        if(isVerified){
            if(avatar.isNullOrEmpty()){
                viewModel.signUpWithoutImage( name, phone, email, password, birthday, address, city, country, token)
            }else {
                viewModel.signUp(name, phone, email, password, birthday, address, city, country, avatar, token)
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(name: String, phone: String, email: String, password: String, birthday: String, address: String, city: String, country: String, avatar: String, token: String): PhoneVerificationFragment {
            val fragment = PhoneVerificationFragment()

            fragment.name = name
            fragment.phone = phone
            fragment.email = email
            fragment.password = password
            fragment.birthday = birthday
            fragment.address = address
            fragment.city = city
            fragment.country = country
            fragment.avatar = avatar
            fragment.token = token

            return fragment
        }
    }
}
