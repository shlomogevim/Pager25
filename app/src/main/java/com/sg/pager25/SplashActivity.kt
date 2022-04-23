package com.sg.pager25

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.content.res.ResourcesCompat
import com.sg.pager25.databinding.ActivitySplashBinding
import com.sg.pager25.firestore.FirestoreClass
import com.sg.pager25.login.activities.BaseActivity
import com.sg.pager25.login.activities.LoginActivity
import com.sg.pager25.post_activities.PostMainActivity
import com.sg.pager25.utilities.FontFamilies

class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
      /*  window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )*/
        setText()
        pauseIt()

    }

    private fun setText() {
        val font= FontFamilies()
        val fontAddress = font.getFamilyFont(103)
        binding.tvAppName.typeface = ResourcesCompat.getFont(this, fontAddress)

        binding.tvAppName.textSize= 22F
        binding.tvAppName.text="זה מה שלימדו אותנו היום בגן ..."
    }
    private fun pauseIt() {
        Handler().postDelayed(
            {
                val currentUserID = FirestoreClass().getCurrentUserID()
         logi("splash 43       currentUserID=$currentUserID")
                if (currentUserID.isNotEmpty()) {
                //  startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))


                  startActivity(Intent(this@SplashActivity, PostMainActivity::class.java))
                } else{
                    startActivity(Intent(this, LoginActivity::class.java))
                   //util.logi("SlashActivity 50 currentUserID=$currentUserID")
                }
                finish()
            },2
        )
    }
}