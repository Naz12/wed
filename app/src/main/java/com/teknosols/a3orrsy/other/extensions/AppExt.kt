package com.teknosols.a3orrsy.other.extensions
/**
 * Various extension functions for AppCompatActivity.
 */

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.Uri
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.befikey.user.other.factory.ViewModelFactory
import com.bumptech.glide.load.engine.Resource
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.view.activites.AdminActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.view.activites.LandingActivity
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Callable


/**
 * The `fragment` is added to the container view with vehicle_id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int,clearStack: Boolean,addToBackstack: Boolean) {
    if(clearStack)
        supportFragmentManager.popBackStack(0,FragmentManager.POP_BACK_STACK_INCLUSIVE)
    supportFragmentManager.transact {
        replace(frameId, fragment)
        if(addToBackstack)
            addToBackStack(null)
    }
}

fun FragmentActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int,clearStack: Boolean,addToBackstack: Boolean) {
    if(clearStack)
        supportFragmentManager.popBackStack(0,FragmentManager.POP_BACK_STACK_INCLUSIVE)
    supportFragmentManager.transact {
        replace(frameId, fragment)
        if(addToBackstack)
            addToBackStack(null)
    }
}

fun Activity.gotoGlobalNavigationActivity() {
    val nextIntent  = Intent(this, GlobalNavigationActivity::class.java)
    nextIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    if(intent.hasExtra("deeplink")) {
        var deeplink = intent.getStringExtra("deeplink")
        if(deeplink.isNotEmpty()) {
            nextIntent.let {
                it.action = Intent.ACTION_VIEW
                it.data = Uri.parse(deeplink)
            }
        }
    }
    startActivity(nextIntent)
    finish()
}


fun Activity.gotoAdminActivity() {
    val nextIntent  = Intent(this, AdminActivity::class.java)
    nextIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    if(intent.hasExtra("deeplink")) {
        var deeplink = intent.getStringExtra("deeplink")
        if(deeplink.isNotEmpty()) {
            nextIntent.let {
                it.action = Intent.ACTION_VIEW
                it.data = Uri.parse(deeplink)
            }
        }
    }
    startActivity(nextIntent)
    finish()
}

fun Activity.gotoLandingActivity() {
    val intent  = Intent(this, LandingActivity::class.java)
    startActivity(intent)
    finish()
}

fun FragmentActivity.gotoLandingActivity() {
    val intent  = Intent(this, LandingActivity::class.java)
    startActivity(intent)
    finish()
}

fun AppCompatActivity.gotoLandingActivity() {
    val intent  = Intent(this, LandingActivity::class.java)
    startActivity(intent)
    finish()
}


/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this, ViewModelFactory.getInstance(this.activity?.application!!)).get(viewModelClass)


/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun isValidPhone(phoneNumber: String): Boolean {
    return (!TextUtils.isEmpty(phoneNumber.trim { it <= ' ' })
            && Patterns.PHONE.matcher(phoneNumber.trim { it <= ' ' }).matches()
            && phoneNumber.length >= 13)
}

fun isEmailValid(target: CharSequence): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun showMapDirection(context: Context,latitude: Double?, longitude: Double?) {
    val intent = Intent(
        android.content.Intent.ACTION_VIEW, Uri.parse(
            "http://maps.google.com/maps?q=loc:$latitude,$longitude"
        )
    )
    context.startActivity(intent)
}

fun shareAppLink(context: Context){
    var i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse("https://play.google.com/store/apps/details?id=com.teknosols.a3orrsy")
    context.startActivity(i)
}

fun getMonthForInt(num: Int): String {
    var month = "wrong"
    val dfs = DateFormatSymbols()
    val months = dfs.months
    if (num >= 0 && num <= 11) {
        month = months[num]
    }
    return month
}

fun isOnline(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    //should check null because in airplane mode it will be null
    return netInfo != null && netInfo.isConnected
}


fun parseDate(time: String): String? {
    val inputPattern = "dd-MM-yyyy"
    val outputPattern = "yyyy-MM-dd"
    val inputFormat = SimpleDateFormat(inputPattern)
    val outputFormat = SimpleDateFormat(outputPattern)

    var date: Date? = null
    var str: String? = null

    try {
        date = inputFormat.parse(time)
        str = outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return str
}

fun parseDateIntoAppFormat(time: String): String? {
    val inputPattern = "yyyy-MM-dd"
    val outputPattern = "dd-MM-yyyy"
    val inputFormat = SimpleDateFormat(inputPattern)
    val outputFormat = SimpleDateFormat(outputPattern)

    var date: Date? = null
    var str: String? = null

    try {
        date = inputFormat.parse(time)
        str = outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return str
}

fun parseDateIntoCurrentDate(time: String): String? {
    val inputPattern = "dd-MM-yyyy HH:mm:ss a"
    val outputPattern = "dd-MM-yyyy"
    val inputFormat = SimpleDateFormat(inputPattern)
    val outputFormat = SimpleDateFormat(outputPattern)

    var date: Date? = null
    var str: String? = null

    try {
        date = inputFormat.parse(time)
        str = outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return str
}

fun showUpdateMessage(context: Context, msg: String, myFunc: Callable<Int>) {
    val builder = AlertDialog.Builder(context!!)
    builder.setTitle(R.string.label_alert)
    builder.setMessage(msg)
        .setCancelable(true)
        .setNegativeButton(
            context.getString(R.string.yes),
            DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
                myFunc.call()
            })
        .setPositiveButton(
            context.getString(R.string.no),
            DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
            })
    val alert = builder.create()
    alert.show()
}
