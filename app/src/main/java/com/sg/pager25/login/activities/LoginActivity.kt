package com.sg.pager25.login.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.sg.pager25.R
import com.sg.pager25.databinding.ActivityLoginBinding
import com.sg.pager25.firestore.FirestoreClass
import com.sg.pager25.general.BaseActivity
import com.sg.pager25.models.User
import com.sg.pager25.utilities.Constants.EXTRA_USER_DETAILS

class LoginActivity : BaseActivity(),View.OnClickListener {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

       DemiData()

        binding.tvRegister.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)


    }

    private fun DemiData() {
        binding.etEmail.setText("shlomo.gevim@gmail.com")
        binding.etPassword.setText("111111")
    }


    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser() {


        if (validateLoginDetails()) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Get the text from editText and trim the space
            val email = binding.etEmail.text.toString().trim { it <= ' ' }
            val password = binding.etPassword.text.toString().trim { it <= ' ' }
           logi("login 64    ======>  email=$email  password=$password")
             FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        logi("login 69 email=$email  password=$password")
                        FirestoreClass().getUserDetails(this)
                        logi("login 71 email=$email  password=$password")

                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_login -> {
                    logInRegisteredUser()
                }
                R.id.tv_register -> {
                   startActivity(Intent(this, RegisterActivity::class.java))
                }
                R.id.tv_forgot_password -> {
                    startActivity(Intent(this, ForgetPasswordActivity::class.java))
                }
            }

        }
    }

    fun userLoggedInSuccess(user: User) {



        hideProgressDialog()

        // if (user.profileCompleted == ) {
       logi("login 105  user==>$user")
        if (user.profileCompleted ==0) {
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(EXTRA_USER_DETAILS, user)
            startActivity(intent)
           // startActivity(Intent(this@LoginActivity, UserProfileActivity::class.java))
        }else{
            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
            intent.putExtra(EXTRA_USER_DETAILS, user)
            startActivity(intent)
        }

        finish()
    }


}
