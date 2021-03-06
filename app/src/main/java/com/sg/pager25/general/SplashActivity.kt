package com.sg.pager25.general

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.content.res.ResourcesCompat
import com.sg.pager25.databinding.ActivitySplashBinding
import com.sg.pager25.firestore.FirestoreClass
import com.sg.pager25.login.activities_appshop.DashboardActivity
import com.sg.pager25.login.activities_appshop.LoginActivity
import com.sg.pager25.login.activities_appshop.MainActivityAppShop
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

    /*    var currentUserID = FirestoreClass().getCurrentUserID()
        logi("splash 41       currentUserID ===>currentUser=$currentUserID  ")*/


        Handler().postDelayed(
            {
                var currentUserID = FirestoreClass().getCurrentUserID()
             logi("SplashAvtivity 42  \n     currentUserID  ===> $currentUserID  ")

                //   currentUserID=""

                startActivity(Intent(this, MainActivityAppShop::class.java))


              /*  if (currentUserID.isNotEmpty()) {
                    startActivity(Intent(this, MainActivityAppShop::class.java))
                } else{
                    startActivity(Intent(this, LoginActivity::class.java))
                }*/
                finish()
            },2
        )
    }
}
/* val currentUserID = FirestoreClass().getCurrentUserID()

                if (currentUserID.isNotEmpty()) {
                    startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                } else{
                    startActivity(Intent(this,LoginActivity::class.java))
                }
                finish()
            },2500*/