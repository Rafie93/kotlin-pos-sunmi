package com.zerone.zerone_pos.ui.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.Const
import com.zerone.zerone_pos.helpers.SharedPrefControl
import kotlinx.android.synthetic.main.activity_edit_merchant.*

class EditMerchantActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_merchant)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initView()
    }

     fun initView() {
         val name = SharedPrefControl.getInstance(applicationContext).getString(Const.MERCHANT_NAME)
         val city = SharedPrefControl.getInstance(applicationContext).getString(Const.MERCHANT_CITY)
         val address = SharedPrefControl.getInstance(applicationContext).getString(Const.MERCHANT_ADDRESS)

         editTextMerchantName.setText(name)
         editTextMerchantCity.setText(city)
         editTextMerchantAddress.setText(address)
     }
}
