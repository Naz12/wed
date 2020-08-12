package com.teknosols.a3orrsy.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.other.extensions.hideKeyboard
import com.teknosols.a3orrsy.other.util.SessionManager
import com.teknosols.a3orrsy.other.util.DialogUtils
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.dialog.AlertMessageDialog

open class BaseFragment: Fragment(){

    lateinit var dialog: AlertDialog
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::sessionManager.isInitialized) {
            sessionManager = SessionManager(context!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!::dialog.isInitialized) {
            /*dialog = SpotsDialog.Builder()
                .setContext(activity)
                .setMessage(R.string.label_loading)
                .setCancelable(false)
                .build()*/
            dialog = DialogUtils.getProgressDialog(context!!)
        }
        if(this is AdminDepositFragment){
            activity?.findViewById<View>(R.id.toolbarLayout)?.visibility = View.GONE
        }else{
            activity?.findViewById<View>(R.id.toolbarLayout)?.visibility = View.VISIBLE
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

    fun getSessionManager(context: Context): SessionManager {
        if (!::sessionManager.isInitialized) {
            sessionManager = SessionManager(context!!)
        }
        return sessionManager
    }

    fun setClickEnable(button: View, enabled: Boolean){
        button.isClickable = enabled
        button.isEnabled = enabled
    }

    fun showAlertDialog(msg: String){
        AlertMessageDialog.newInstance(msg).show(childFragmentManager, AlertMessageDialog.TAG)
    }

    fun showToast(message: String){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(resId: Int){
        Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()

        if(isRemoving) view!!.hideKeyboard()
    }

    fun onBackPress(){
        activity?.onBackPressed()
    }

    fun getSimpleName(): String{
        return this.javaClass.simpleName
    }

    fun clearStack(){
        val fm = (context as BaseActivity).supportFragmentManager
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun checkBookingOrAdvertisement(categoryId : Int) : String{
        if(categoryId == 1 || categoryId == 2 || categoryId == 3 || categoryId == 5 || categoryId == 11 || categoryId == 14){
            return "booking"
        }else
            return "add"
    }
}