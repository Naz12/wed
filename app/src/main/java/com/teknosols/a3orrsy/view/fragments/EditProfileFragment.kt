package com.teknosols.a3orrsy.view.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.ImageResponse
import com.teknosols.a3orrsy.datamodel.model.response.wrappers.UserResponse
import com.teknosols.a3orrsy.other.extensions.OneShotEvent
import com.teknosols.a3orrsy.other.extensions.getMonthForInt
import com.teknosols.a3orrsy.other.extensions.gotoGlobalNavigationActivity
import com.teknosols.a3orrsy.other.extensions.obtainViewModel
import com.teknosols.a3orrsy.other.util.DatePickerForDOB
import com.teknosols.a3orrsy.view.activites.BaseActivity
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.viewmodel.EditProfileViewModel
import com.teknosols.a3orrsy.viewmodel.SignInViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_global_navigation.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.ivCamera
import kotlinx.android.synthetic.main.fragment_edit_profile.ivDp
import kotlinx.android.synthetic.main.fragment_edit_profile.rlImage
import kotlinx.android.synthetic.main.fragment_edit_profile.tvName
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.navigation_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*


class EditProfileFragment : BaseFragment(), View.OnClickListener{

    var imageUrl: String = ""
    lateinit var viewModel: EditProfileViewModel

    override fun onClick(v: View?) {
        when(v){
            tvChangePassword ->{
                val changePasswordFragment = ChangePasswordFragment.newInstance()
                (activity as BaseActivity).replaceFragment(changePasswordFragment,changePasswordFragment.getSimpleName(),true,false)
            }

            tvDateOfBirth ->{
                val newDatePicker = DatePickerForDOB()
                newDatePicker.setDateSetListner { view, year, month, day ->
                    tvDateOfBirth.setText("${Integer.toString(day)} ${getMonthForInt(month)} ${Integer.toString(year)}")
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

            btnUpdate ->{
                viewModel.updateUserProfile(sessionManager.getUserId(),tvName.text.toString(), tvPhone.text.toString(),
                                            tvEmail.text.toString(), tvDateOfBirth.text.toString(), tvAddress.text.toString(),
                                            tvCity.text.toString(), tvCountry.text.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = obtainViewModel(EditProfileViewModel::class.java)

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

        viewModel.imageResponse.observe(viewLifecycleOwner, object : Observer<OneShotEvent<ImageResponse>> {
            override fun onChanged(t: OneShotEvent<ImageResponse>?) {
                var show = t?.getContentIfNotHandled()
                show?.let {
                    Glide.with(context!!)
                        .load(it.data.image)
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivDp)

                    Glide.with(context!!).load(it.data.image)
                        .apply(RequestOptions.circleCropTransform())
                        .into((activity as BaseActivity).ivUserImage)

                    sessionManager.setProfileImage(it.data.image)
                    showAlertDialog(it.message)
                }
            }
        })

        viewModel.updatedUser.observe(viewLifecycleOwner, object : Observer<OneShotEvent<UserResponse>> {
            override fun onChanged(t: OneShotEvent<UserResponse>?) {
                var show = t?.getContentIfNotHandled()
                show?.let {
                    sessionManager.setFirstName(it!!.data.name)
                    sessionManager.setEmail(it!!.data.email)
                    sessionManager.setPhone(it!!.data.phone)
                    sessionManager.setProfileImage(it!!.data.avatar)
                    sessionManager.setBirthday(it!!.data.birthday)
                    sessionManager.setAddressLine1(it!!.data.address)
                    sessionManager.setCity(it!!.data.city)
                    sessionManager.setCountry(it!!.data.country)


                    (activity as BaseActivity).tvProfileName.text = sessionManager.getFirstName()
                    (activity as BaseActivity).showAlertDialog(it.message)
                    onBackPress()
                }
            }
        })

        (activity as GlobalNavigationActivity).toolbarLayout.header_textview.text = "Update Profile"
        setProfile()

        tvChangePassword.setOnClickListener(this)
        tvDateOfBirth.setOnClickListener(this)
        rlImage.setOnClickListener(this)
        btnUpdate.setOnClickListener(this)
    }

    fun setProfile(){
        if(!sessionManager.getProfileImage().isNullOrEmpty()) {
            Glide.with(context!!).load(sessionManager.getProfileImage())
                .apply(RequestOptions.circleCropTransform())
                .into(ivDp)
        }

        tvName.setText(sessionManager.getFirstName())
        tvPhone.setText(sessionManager.getPhone())
        tvEmail.setText(sessionManager.getEmail())
        tvDateOfBirth.setText(sessionManager.getBirthday())
        tvAddress.setText(sessionManager.getAddressLine1())
        tvCity.setText(sessionManager.getCity())
        tvCountry.setText(sessionManager.getCountry())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                imageUrl = result.uri.toString()
                Log.i("resultURL", result.uri.toString())
                imageUrl = imageUrl.substring(7)
                Log.i("resultURL", imageUrl)

               viewModel.uploadImage(sessionManager.getUserId(),imageUrl)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditProfileFragment()
    }

}
