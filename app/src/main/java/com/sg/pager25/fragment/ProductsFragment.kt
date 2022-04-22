package com.sg.pager25.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.sg.pager25.R
import com.sg.pager25.activities.SettingActivity


class ProductsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

         val view= inflater.inflate(R.layout.fragment_products, container, false)
        val  tv=view.findViewById<Button>(R.id.textView)
        tv.text=" Well come to Prouduct  fragment"
        val btnToSetting=view.findViewById<Button>(R.id.settingBtn)
        btnToSetting.setOnClickListener {
            startActivity(Intent(context, SettingActivity::class.java))
        }



        return view





        return inflater.inflate(R.layout.fragment_products, container, false)
    }


}