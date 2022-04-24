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
import com.sg.pager25.utilities.GlideLoader

class SettingActivity : BaseActivity(),View.OnClickListener {
    lateinit var binding: ActivitySettingBinding
    private lateinit var currentUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = intent.getParcelableExtra(Constants.USER_EXTRA)!!
        logi("SettingActivity   25 =======>  /n $currentUser  ")

        binding.tvEdit.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
    }
    override fun onResume() {  // when we load app
        super.onResume()

        getUserDetails()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.tv_edit -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    intent.putExtra(EXTRA_USER_DETAILS, currentUser)
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


    /**
     * A function to get the user details from firestore.
     */
    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        // Call the function of Firestore class to get the user details from firestore which is already created.
        FirestoreClass().getUserDetails(this)
    }

    /**
     * A function to receive the user details and populate it in the UI.
     */
    fun userDetailsSuccess(user: User) {

        currentUser = user
        hideProgressDialog()

        logi("setting 69 user=$user")

        GlideLoader(this).loadUserPicture(user.image, binding.ivUserPhoto)

        binding.tvName.text = "${user.firstName} ${user.lastName}"
        binding.tvGender.text = user.gender
        binding.tvEmail.text = user.email
        binding.tvMobileNumber.text = "${user.mobile}"
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