package com.zerone.zerone_pos.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.Const
import com.zerone.zerone_pos.helpers.SharedPrefControl
import com.zerone.zerone_pos.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register_success.*

class RegisterSuccessActivity : AppCompatActivity() {
    lateinit var smallToBig: Animation
    lateinit var transalteToTop: Animation
    lateinit var btnAnim: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_success)

        smallToBig = AnimationUtils.loadAnimation(this,R.anim.small_to_big)
        transalteToTop = AnimationUtils.loadAnimation(this,R.anim.translate_to_top)
        btnAnim = AnimationUtils.loadAnimation(this,R.anim.button_anim)

        image.startAnimation(smallToBig)
        subtitle.startAnimation(transalteToTop)
        startLogin.startAnimation(btnAnim)

        startLogin.setOnClickListener {
            val intent = Intent(this@RegisterSuccessActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
