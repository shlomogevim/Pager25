package com.sg.pager25.login.activities_appshop

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.sg.pager25.R
import com.sg.pager25.databinding.ActivitySettingBinding
import com.sg.pager25.firestore.FirestoreClass
import com.sg.pager25.general.BaseActivity
import com.sg.pager25.models.User
import com.sg.pager25.utilities.Constants
import com.sg.pager25.utilities.Constants.EXTRA_USER_DETAILS
import com.sg.pager25.utilities.Constants.USER_EXTRA
import com.sg.pager25.utilities.GlideLoader

class SettingActivity : BaseActivity(),View.OnClickListener {
    lateinit var binding: ActivitySettingBinding
    private lateinit var currentUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = intent.getParcelableExtra(Constants.USER_EXTRA)!!
       // logi("SettingActivity   26 =======>  /n $currentUser  ")

        binding.tvEditProfile.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
    }
    override fun onResume() {  // when we load app
        super.onResume()
        drawUserDetails()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.tv_editProfile -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    intent.putExtra(USER_EXTRA, currentUser)
                    startActivity(intent)
                }
                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK  // clear all layers in the stuck
                    startActivity(intent)
                    finish()
                }
            }
        }
    }


       private fun drawUserDetails() {
//        showProgressDialog(resources.getString(R.string.please_wait))
//        // Call the function of Firestore class to get the user details from firestore which is already created.
//        FirestoreClass().getUserDetails(this)
           GlideLoader(this).loadUserPicture(currentUser.image, binding.ivUserPhoto)

           binding.tvUserName.text = currentUser.userName
           binding.tvLastName.text = currentUser.nickName
           binding.tvMail.text = currentUser.email
           binding.tvGender.text =currentUser.gender
          binding.tvMoto.text=currentUser.moto
    }


}

/**
 * A function for actionBar Setup.
 */
/*private fun setupActionBar() {

    setSupportActionBar(toolbar_settings_activity)

    val actionBar = supportActionBar
    if (actionBar != null) {
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
    }

    toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
}*/