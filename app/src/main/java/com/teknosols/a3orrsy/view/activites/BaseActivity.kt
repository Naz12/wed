package com.teknosols.a3orrsy.view.activites

import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.teknosols.a3orrsy.other.util.SessionManager
import com.teknosols.a3orrsy.view.dialog.AlertMessageDialog
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.other.application.MyContextWrapper
import com.teknosols.a3orrsy.other.util.DialogUtils
import java.util.*

open class BaseActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager;
    lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        if (!::dialog.isInitialized) {
            /*dialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.label_loading)
                .setCancelable(false)
                .build()*/
            dialog = DialogUtils.getProgressDialog(this!!)
        }
    }

    fun showProgressDialog(show: Boolean){
        if(dialog != null && show) {
            if(!dialog.isShowing)
                dialog.apply {
                    show()
                }
        }else if(dialog != null && !show){
            if(dialog.isShowing)
                dialog.dismiss()
        }
    }

    fun showAlertDialog(msg: String){
        AlertMessageDialog.newInstance(msg).show(supportFragmentManager, AlertMessageDialog.TAG)
    }

    fun popCurrenFragment(){
        supportFragmentManager.popBackStack()
    }



    fun replaceFragment(fragment: Fragment, tag: String, addToStack: Boolean, clearStack: Boolean){
        if(clearStack)
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        var ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container,fragment,tag)
        if(addToStack)
            ft.addToBackStack(tag)
        ft.commit()
    }

    override fun attachBaseContext(newBase: Context?) {
        val newLocale = Locale(SessionManager(newBase!!).getLocale())
        val context = MyContextWrapper.wrapApplication(newBase, newLocale)
        super.attachBaseContext(context)
    }

    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        //should check null because in airplane mode it will be null
        return netInfo != null && netInfo.isConnected
    }

    fun clearStack(){
        val fm = supportFragmentManager
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    /////////////////////// Language Changer /////////////////////////////

    fun showChangeLanguageDialog() {

        val listItems = arrayOf("English","العربية")
        val mBuilder = android.app.AlertDialog.Builder(this)
        mBuilder.setTitle(getString(R.string.choose_language))
//        mBuilder.setPositiveButton(getString(R.string.continue_cap), null)
//        mBuilder.setNegativeButton(getString(R.string.cancel), null)
        mBuilder.setSingleChoiceItems(listItems, getCheckedItem(listItems) ,
            DialogInterface.OnClickListener { dialogInterface, i ->
                if (i == 0) {
                    setLocale("en")
                    recreate()
                } else if (i == 1) {
                    setLocale("ar")
                    recreate()
                }

                dialogInterface.dismiss()
            })

        val mDialog = mBuilder.create()
//        mDialog.setCancelable(false)
        mDialog.show()
    }

    private fun getCheckedItem(listItems: Array<String>): Int {
        if(sessionManager.getLanguage().equals("en") || sessionManager.getLanguage().equals(""))
            return 0
        else
            return 1
    }

    fun setLocale(lang: String?) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        this.getResources()
            .updateConfiguration(config, this.getResources().getDisplayMetrics())

        sessionManager.setLanguage(lang!!)

    }

    fun loadLocale() {
        val language = sessionManager.getLanguage()
        setLocale(language)
    }

    fun setDefaultLanguage(){

        val language = sessionManager.getLanguage()

        if (language.isNullOrEmpty()) {
            Locale.getDefault().displayLanguage
            Log.i("Lang", Locale.getDefault().displayLanguage)
        } else {
            loadLocale()
        }
    }

}