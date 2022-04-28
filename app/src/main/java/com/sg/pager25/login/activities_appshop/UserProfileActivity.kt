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
import com.bumptech.glide.Glide
import com.sg.pager25.R
import com.sg.pager25.databinding.ActivityUserProfileBinding
import com.sg.pager25.firestore.FirestoreClass
import com.sg.pager25.general.BaseActivity
import com.sg.pager25.models.User
import com.sg.pager25.utilities.Constants
import com.sg.pager25.utilities.Constants.COMPLETE_PROFILE
import com.sg.pager25.utilities.Constants.FEMALE
import com.sg.pager25.utilities.Constants.IMAGE
import com.sg.pager25.utilities.Constants.LASTNAME
import com.sg.pager25.utilities.Constants.MALE
import com.sg.pager25.utilities.Constants.MOBILE
import com.sg.pager25.utilities.Constants.PICK_IMAGE_REQUEST_CODE
import com.sg.pager25.utilities.Constants.READ_STORAGE_PERMISSION_CODE
import com.sg.pager25.utilities.Constants.USERNAME
import com.sg.pager25.utilities.Constants.USER_EXTRA
import com.sg.pager25.utilities.Constants.USER_GENDER
import com.sg.pager25.utilities.Constants.USER_MOTO
import com.sg.pager25.utilities.GlideLoader
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
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

        currentUser=intent.getParcelableExtra(USER_EXTRA)!!
//        logi("UserProfileActivity 48          currentUser=$currentUser")

       dratExistData()
        binding.ivUserPhoto.setOnClickListener {
            findImage()
        }
        binding.btnSave.setOnClickListener {
            saveDetails()
        }
    }

    private fun dratExistData() {
   //     logi("profile 55   currentUser=$currentUser")
        binding.tvUserName.setText(currentUser.userName)
        binding.tvLastName.setText(currentUser.nickName)
        binding.tvGender.setText(currentUser.gender)
       binding.tvMoto.setText(currentUser.moto)

        GlideLoader(this@UserProfileActivity).loadUserPicture(currentUser.image,binding.ivUserPhoto)
    }


    private fun saveDetails() {

        if (validateUserProfileDetails()) {
            // submitBtnInAction()
            showProgressDialog(resources.getString(R.string.please_wait))
          //  logi("UserProileActivity  74      mSelectedImageFileUri=$mSelectedImageFileUri")

            if (mSelectedImageFileUri != null) {
              // logi("UserProileActivity  75      mSelectedImageFileUri=$mSelectedImageFileUri")
                FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
            } else {
                updateUserProfileDetails()
            }
        }
    }

    private fun findImage() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            // showErrorSnackBar("You have already storage permission",false)
            Constants.showImageChooser(this@UserProfileActivity)
        } else {
               ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()

        val firstName = binding.tvUserName.text.toString().trim { it <= ' ' }
        if (firstName != currentUser.userName) {
            userHashMap[USERNAME] = firstName
        }
        val lastName = binding.tvLastName.text.toString().trim { it <= ' ' }
        if (lastName != currentUser.nickName) {
            userHashMap[LASTNAME] = lastName
        }
        val gender = binding.tvGender.text.toString().trim { it <= ' ' }
        if (gender != currentUser.gender) {
            userHashMap[USER_GENDER] = gender
        }
        val moto = binding.tvMoto.text.toString().trim { it <= ' ' }
        if (moto != currentUser.moto) {
            userHashMap[USER_MOTO] = moto
        }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[IMAGE] = mUserProfileImageURL
        }


            //  logi("UserProfileActivity  130        userHashMap=$userHashMap")
        FirestoreClass().updateUserProfileData(this@UserProfileActivity, userHashMap)
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this@UserProfileActivity, SettingActivity::class.java))
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
//                showErrorSnackBar("הכנס שם משתמש", true)
                showErrorSnackBar(resources.getString(R.string.empty_userName_tv), true)
                false
            }
            TextUtils.isEmpty(binding.tvLastName.text.toString().trim { it <= ' ' }) -> {
              //  showErrorSnackBar("הכנס כינוי", true)
                showErrorSnackBar(resources.getString(R.string.empty_nickName_tv), true)
                false
            }
            else -> {
                true
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PICK_IMAGE_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let { uri ->
                        launchImageCrop(uri)
                    }
                } else {
                    Log.d("ImageCropping", "onActivityResult: Couldn't select that image from gallery")
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    setImage(result.uri)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Log.d("ImageCropping", "onActivityResult: ${result.error}")
                }
            }
        }

    }
    private fun launchImageCrop(uri: Uri) {
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
          // .setAspectRatio(1920, 1080)
            .setAspectRatio(1, 1)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(this)
    }

    private fun setImage(uri: Uri) {
        mSelectedImageFileUri=uri
        Glide.with(this)
            .load(uri)
            .into(binding.ivUserPhoto)
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



/* public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
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
    }*/


