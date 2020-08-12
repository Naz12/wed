package com.teknosols.a3orrsy.view.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.teknosols.a3orrsy.other.extensions.*
import com.teknosols.a3orrsy.other.util.DatePickerForDOB
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.activites.LandingActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.sign_header.*
import android.widget.ArrayAdapter
import java.util.*
import kotlin.collections.ArrayList
import android.widget.AdapterView
import com.teknosols.a3orrsy.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class SignUpFragment : BaseFragment(), View.OnClickListener{

    lateinit var token: String
    var imageUrl: String = ""
    var countryList : ArrayList<String> = ArrayList()
    var cityList : ArrayList<String> = ArrayList()

    override fun onClick(v: View?) {
        when(v){
            btnBack ->{
                onBackPress()
            }
            btnSignUp ->{
                validationChecks()
            }
            etDOB ->{
                val newDatePicker = DatePickerForDOB()
                newDatePicker.setDateSetListner { view, year, month, day ->
                    etDOB.setText("${Integer.toString(day)} ${getMonthForInt(month)} ${Integer.toString(year)}")
                }
                newDatePicker.show(fragmentManager, "datePicker")
            }
            rlImage ->{
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setActivityTitle("My Crop")
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setCropMenuCropButtonTitle("Done")
                    .setRequestedSize(800, 800)
                    .start(activity!!)
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
        var view = inflater.inflate(com.teknosols.a3orrsy.R.layout.fragment_sign_up, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvHeader?.text = getString(R.string.sign_up)
        btnBack.setOnClickListener(this)
        btnSignUp.setOnClickListener(this)
        etDOB.setOnClickListener(this)
        rlImage.setOnClickListener(this)
        btnChangeLanguage.setOnClickListener(this)
        setSpinnerData()

        spCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cityList.clear()
                if(!spCountry.selectedItem.toString().equals("Select Country")){
                    spCity.visibility = View.VISIBLE
                    cityList.add(getString(R.string.select_city))
                    //set cities arrayList of selected country
                    setCitiesArrayList()
                    cityList.removeAt(cityList.size-1)
                    //set cities spinner data
                    val citiesListAdapter = ArrayAdapter<String>(context ,R.layout.spinner_item , cityList)
                    citiesListAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    spCity.setAdapter(citiesListAdapter)
                }else{
                    spCity.visibility = View.GONE
                }
            }
        }

    }

    private fun setSpinnerData() {

        //set Countries spinner data
        countryList.add(getString(R.string.select_country))
        setCountriesArrayList()
        countryList.removeAt(countryList.size-1)
        val countryListAdapter = ArrayAdapter<String>(context, R.layout.spinner_item, countryList)
        countryListAdapter.setDropDownViewResource(com.teknosols.a3orrsy.R.layout.spinner_dropdown_item)
        spCountry.setAdapter(countryListAdapter)

    }

    private fun setCountriesArrayList() {
        var inputStream = context!!.assets.open("cities")
        val isReader = InputStreamReader(inputStream)
        val reader = BufferedReader(isReader)
        var sb =  StringBuilder()

        var line =  reader.readLine()
        try {
            while (!line.isNullOrEmpty()) {
                sb.append(line + "\n")
                line =  reader.readLine()
            }
        } catch ( e:IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
            } catch ( e: IOException) {
                e.printStackTrace()
            }
        }
        val str = sb.toString()
        val jsonResponse = JSONObject(str)
        val jsonCountryArray = jsonResponse.names()
        Log.i("MyCountries: ", jsonCountryArray.toString())

        for (i in 0..jsonCountryArray.length()){
            var jsonObject = jsonCountryArray.optString(i)
            if(!jsonObject.isNullOrEmpty())
                countryList.add(jsonObject)
            Log.i("MyCountry: $i ", jsonObject)
        }

    }

    private fun setCitiesArrayList() {

        var inputStream = context!!.assets.open("cities")
        val isReader = InputStreamReader(inputStream)
        val reader = BufferedReader(isReader)
        var sb =  StringBuilder()

       var line =  reader.readLine()
        try {
            while (!line.isNullOrEmpty()) {
                sb.append(line + "\n")
                line =  reader.readLine()
            }
        } catch ( e:IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
            } catch ( e: IOException) {
                e.printStackTrace()
            }
        }

        Log.i("Line" , sb.toString())
        val str = sb.toString()
        val jsonResponse = JSONObject(str)
        var jsonCitiesArray = jsonResponse.getJSONArray(spCountry.selectedItem.toString())
        Log.i("MyCities: ", jsonCitiesArray.toString())

        for (i in 0..jsonCitiesArray.length()){
            var jsonObject = jsonCitiesArray.optString(i)
            cityList.add(jsonObject)
            Log.i("MyCity: $i ", jsonObject)
        }
    }

    fun validationChecks(){
        if(etFirstName.text.isNullOrEmpty()){
            etFirstName.error = "Enter First Name"
            etFirstName.requestFocus()
        }else if(etLastName.text.isNullOrEmpty()){
            etLastName.error = "Enter Last Name"
            etLastName.requestFocus()
        }else if(etPhoneNo.text.isNullOrEmpty()){
            etPhoneNo.error = "Enter Phone No"
            etPhoneNo.requestFocus()
        }else if(!isEmailValid(etEmail.text.toString().trim()) && !etEmail.text.isNullOrEmpty()){
            etEmail.error = "Invalid Email"
            etEmail.requestFocus()
        }else if(etDOB.text.isNullOrEmpty()){
            etDOB.error = "Enter Date Of Birth"
            etDOB.requestFocus()
        }else if(etPassword.text.isNullOrEmpty()){
            etPassword.error = "Enter Password"
            etPassword.requestFocus()
        }else if(etConfirmPassword.text.isNullOrEmpty()){
            etConfirmPassword.error = "Enter Confirm Password"
            etConfirmPassword.requestFocus()
        }else if(etPassword.text.length < 6){
            etPassword.error = "Enter Minimum 6 Characters"
            etPassword.requestFocus()
        }else if(etConfirmPassword.text.toString() != etPassword.text.toString()){
            etConfirmPassword.error = "Password Does Not Match"
            etConfirmPassword.requestFocus()
        }else if(etAddress.text.isNullOrEmpty()){
            etAddress.error = "Enter Address"
            etAddress.requestFocus()
        }else if(spCountry.selectedItem.toString().equals("Select Country")){
            spCountry.requestFocusFromTouch()
            spCountry.performClick()
        }else if(spCity.visibility != View.GONE && spCity.selectedItem.toString().equals("Select City") ){
            spCity.requestFocusFromTouch()
            spCity.performClick()
        }else{
            val phoneVerificationFragment = PhoneVerificationFragment.newInstance("${etFirstName.text} ${etLastName.text}", ccp.selectedCountryCodeWithPlus+etPhoneNo.text.toString(), etEmail.text.toString().trim(),
                etPassword.text.toString(), etDOB.text.toString(), etAddress.text.toString(),
                spCity.selectedItem.toString(), spCountry.selectedItem.toString(), imageUrl,token)
            (activity as BaseActivity).replaceFragment(phoneVerificationFragment,phoneVerificationFragment.getSimpleName(),true,false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {

                imageUrl = result.uri.toString()
                Log.i("resultURL", result.uri.toString())
                imageUrl = imageUrl.substring(7)
                Log.i("resultURL", imageUrl)

                Glide.with(this).load(result.uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivDp)
                ivCamera.visibility = View.GONE


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun isEmailValid(target: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    companion object {
        @JvmStatic
        fun newInstance(token: String): SignUpFragment {
            val fragment = SignUpFragment()
            val bundle = Bundle()
            bundle.putSerializable("token", token)
            fragment.arguments = bundle
            return fragment
        }
    }
}
