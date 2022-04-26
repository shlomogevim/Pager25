package com.sg.pager25.login.activities_appshop

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sg.pager25.R
import com.sg.pager25.databinding.ActivityUserProfileBinding
import com.sg.pager25.firestore.FirestoreClass
import com.sg.pager25.general.BaseActivity
import com.sg.pager25.models.User
import com.sg.pager25.utilities.Constants
import com.sg.pager25.utilities.Constants.COMPLETE_PROFILE
import com.sg.pager25.utilities.Constants.FEMALE
import com.sg.pager25.utilities.Constants.GENDER
import com.sg.pager25.utilities.Constants.IMAGE
import com.sg.pager25.utilities.Constants.MALE
import com.sg.pager25.utilities.Constants.MOBILE
import com.sg.pager25.utilities.Constants.READ_STORAGE_PERMISSION_CODE
import com.sg.pager25.utilities.Constants.USERNAME
import com.sg.pager25.utilities.Constants.USER_EXTRA
import com.sg.pager25.utilities.GlideLoader
import java.io.IOException

class UserProfileActivity : BaseActivity() {
    lateinit var binding: ActivityUserProfileBinding
    lateinit var currentUser: User

    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logi("UserProfileActivity 43")

       currentUser=intent.getParcelableExtra(USER_EXTRA)!!
//
       getExsistData()
      //  operateAllButtons()
    }

    private fun getExsistData() {

        logi("profile 55   currentUser=$currentUser")


        binding.tvUserName.setText(currentUser.userName)
        binding.tvLastName.setText(currentUser.lastName)
        binding.tvGender.setText(currentUser.gender)
      //  binding.tvMoto.setText(currentUser.dio)

        GlideLoader(this@UserProfileActivity).loadUserPicture(currentUser.image,binding.ivUserPhoto)

//        binding.etEmail.isEnabled = false
//        binding.etEmail.setText(currentUser.email)




//        if (currentUser.profileCompleted == 0) {
//            binding.tvTitle.text = resources.getString(R.string.title_complete_profile)
//            binding.etFirstName.isEnabled = false
//            binding.etLastName.isEnabled = false
//        }else{
//            setupActionBar()
//            binding.tvTitle.text = resources.getString(R.string.title_edit_profile)
//
//            GlideLoader(this@UserProfileActivity).loadUserPicture(currentUser.image,binding.ivUserPhoto)
//
//            // Set the existing values to the UI and allow user to edit except the Email ID.
//
//            if (currentUser.mobile != 0L) {
//                binding.etMobileNumber.setText(currentUser.mobile.toString())
//            }
//            if (currentUser.gender == MALE) {
//                binding.rbMale.isChecked = true
//            } else {
//                binding.rbFemale.isChecked = true
//            }
//        }
    }
    private fun setupActionBar() {

        /*    setSupportActionBar(toolbar_user_profile_activity)

            val actionBar = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            }

            toolbar_user_profile_activity.setNavigationOnClickListener { onBackPressed() }*/
    }

    private fun operateAllButtons() {
        binding.ivUserPhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                // showErrorSnackBar("You have already storage permission",false)
                Constants.showImageChooser(this@UserProfileActivity)
            } else {
                /*Requests permissions to be granted to this application. These permissions
                 must be requested in your manifest, they should not be granted to your app,
                 and they should have protection level*/
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }
        binding.btnSave.setOnClickListener {
            if (validateUserProfileDetails()) {
                // submitBtnInAction()
                showProgressDialog(resources.getString(R.string.please_wait))
                if (mSelectedImageFileUri != null) {
                    FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
                } else {
                    updateUserProfileDetails()
                }
            }
        }
    }

    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()

        val firstName = binding.tvUserName.text.toString().trim { it <= ' ' }
        if (firstName != currentUser.userName) {
            userHashMap[USERNAME] = firstName
        }
        val lastName = binding.tvLastName.text.toString().trim { it <= ' ' }
        if (lastName != currentUser.lastName) {
            userHashMap[Constants.LASTNAME] = lastName
        }

//        val mobileNumber = binding.etMobileNumber.text.toString().trim { it <= ' ' }
//        if (mobileNumber.isNotEmpty() && mobileNumber != currentUser.mobile.toString()) {
//            userHashMap[MOBILE] = mobileNumber.toLong()
//        }

//        val gender = if (binding.rbMale.isChecked) {
//            MALE
//        } else {
//            FEMALE
//        }
//        if (gender.isNotEmpty() && gender != currentUser.gender) {
//            userHashMap[GENDER] = gender
//        }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[IMAGE] = mUserProfileImageURL
        }
        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[IMAGE] = mUserProfileImageURL
        }
        // Here if user is about to complete the profile then update the field or else no need.
        // 0: User profile is incomplete.
        // 1: User profile is completed.

      //  userHashMap[COMPLETE_PROFILE] = 1


        // call the reg isterUser function of FireStore class to make an entry in the database.
        FirestoreClass().updateUserProfileData(this@UserProfileActivity, userHashMap)
    }

    /**
     * A function to notify the success result and proceed further accordingly after updating the user details.
     */
    fun userProfileUpdateSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {
        hideProgressDialog()
        //Toast.makeText(this,"Your image is uploaded successful, imageUrl=$imageURL",Toast.LENGTH_LONG).show()
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {

            TextUtils.isEmpty(binding.tvUserName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("הכנס שם משתמש", true)
                false
            }
            TextUtils.isEmpty(binding.tvLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("הכנס כינוי", true)
                false
            }
            else -> {
                true
            }
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        // The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!
                        // binding.ivUserPhoto.setImageURI(selectedImageFileUri)
                        GlideLoader(this@UserProfileActivity).loadUserPicture(
                            mSelectedImageFileUri!!, binding.ivUserPhoto
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@UserProfileActivity)
                // showErrorSnackBar("The storage permission is grated",false)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}


