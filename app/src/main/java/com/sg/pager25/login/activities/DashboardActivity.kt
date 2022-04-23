package com.sg.pager25.login.activities

import android.content.Intent
import android.os.Bundle
import com.sg.pager25.databinding.ActivityDashboardBinding

class DashboardActivity : BaseActivity() {
    lateinit var binding: ActivityDashboardBinding
   /* private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.products -> {
                    moveToFragment(ProductsFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.dashboard -> {
                    moveToFragment(DashboardFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.orders -> {
                    moveToFragment(OrdersFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding.settingBtn.setOnClickListener {
           startActivity(Intent(this,SettingActivity::class.java))
       }
       binding.loginBtn.setOnClickListener {
           startActivity(Intent(this,LoginActivity::class.java))
       }
      /*  binding.navButton.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        moveToFragment(ProductsFragment())*/

    }
   /* private fun moveToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .addToBackStack("popy")
            .replace(R.id.container, fragment!!)
            //.addToBackStack("tag")
            .commit()
    }*/

    override fun onBackPressed() {
        doubleBackToExit()

    }
}