package com.sg.pager25.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sg.pager25.R
import com.sg.pager25.databinding.ActivityDashboardBinding
import com.sg.pager25.fragment.DashboardFragment
import com.sg.pager25.fragment.OrdersFragment
import com.sg.pager25.fragment.ProductsFragment

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