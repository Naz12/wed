package com.teknosols.a3orrsy.view.activites

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.teknosols.a3orrsy.other.extensions.gotoGlobalNavigationActivity
import com.teknosols.a3orrsy.other.extensions.replaceFragmentInActivity
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.other.extensions.gotoAdminActivity
import com.teknosols.a3orrsy.view.fragments.HomeFragment
import com.teknosols.a3orrsy.view.fragments.SignInFragment
import com.teknosols.a3orrsy.view.fragments.SplashFragment
import java.util.*
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class LandingActivity : BaseActivity() {

    var token: String = ""

    companion object {
        val SPLASH_DELAY: Long = 3000
        val SKIP_SPLASH: String = "skip_splash"
        val START_UP_MESSAGe: String = "start_up_message"
    }
    var handler: Handler? = null
    var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)


        getToken()
        val skipSplash = intent.getBooleanExtra(SKIP_SPLASH,false)
        startSplash(skipSplash)
    }

    fun startSplash(skip:Boolean) {
        if(skip) {
            if (sessionManager.isLoggedIn()) {
                var loginAs = sessionManager.getLoginAs()
                when(loginAs){
                    "admin" ->{
                        setDefaultLanguage()
                        gotoAdminActivity()
                    }
                    "customer" ->{
                        setDefaultLanguage()
                        gotoGlobalNavigationActivity()
                    }
                    "manager" ->{

                    }
                    else ->{
                        showAlertDialog("No login_as status found!!")
                    }
                }
            } else {
                if(sessionManager.getLanguage() == ""){
                    showChangeLanguageDialog()
                }else{
                    setDefaultLanguage()
                }
                val signInFragment = SignInFragment.newInstance(token)
                    replaceFragment(signInFragment,signInFragment.getSimpleName(),false,true)
            }
        }else{
            replaceFragmentInActivity(obtainSplashFragment(), R.id.fragment_container, true, false)
            handler = Handler()
            runnable = Runnable {
                if (sessionManager.isLoggedIn()) {
                    var loginAs = sessionManager.getLoginAs()
                    when(loginAs){
                        "admin" ->{
                            setDefaultLanguage()
                            gotoAdminActivity()
                        }
                        "customer" ->{
                            setDefaultLanguage()
                            gotoGlobalNavigationActivity()
                        }
                        "manager" ->{

                        }
                        else ->{
                            showAlertDialog("No login_as status found!!")
                        }
                    }
                } else {
                    if(sessionManager.getLanguage() == ""){
                        showChangeLanguageDialog()
                    }else{
                        setDefaultLanguage()
                    }
                    val signInFragment = SignInFragment.newInstance(token)
                    replaceFragment(signInFragment,signInFragment.getSimpleName(),false,true)
                }
            }
            handler!!.postDelayed(runnable, SPLASH_DELAY)
        }
    }

    private fun obtainSplashFragment(): SplashFragment {
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(fragment  != null && fragment is SplashFragment)
            return fragment
        else
            return SplashFragment()
    }

    private fun getToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("", "getInstanceId failed", task.exception)
                    showAlertDialog("Firebase inititialization failed")
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                // task.result!!.token
                Log.i("Get_FCM_Token",  task.result!!.token)
                token = task.result!!.token

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        fragment!!.onActivityResult(requestCode, resultCode, data)
    }


}
