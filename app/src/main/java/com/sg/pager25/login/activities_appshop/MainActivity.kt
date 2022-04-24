package com.sg.pager25.login.activities_appshop

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sg.pager25.databinding.ActivityMainBinding
import com.sg.pager25.utilities.Constants.LOGGED_IN_USERNAME
import com.sg.pager25.utilities.Constants.MYSHOPPAL_PREFERENCES

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUsername()
    }

    private fun getUsername() {
        val sharedPreferences =
            getSharedPreferences(MYSHOPPAL_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(LOGGED_IN_USERNAME, "")!!
        binding.textView.text = "Wellcome Mr.   $username."
    }
}
