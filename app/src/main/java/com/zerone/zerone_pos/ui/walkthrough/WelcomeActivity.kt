package com.zerone.zerone_pos.ui.walkthrough
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.Const
import com.zerone.zerone_pos.helpers.SharedPrefControl
import com.zerone.zerone_pos.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    lateinit var smallToBig:Animation
    lateinit var transalteToTop:Animation
    lateinit var btnAnim:Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_welcome)

        smallToBig = AnimationUtils.loadAnimation(this,R.anim.small_to_big)
        transalteToTop = AnimationUtils.loadAnimation(this,R.anim.translate_to_top)
        btnAnim = AnimationUtils.loadAnimation(this,R.anim.button_anim)

        image_intro1.startAnimation(smallToBig)
        headertitle.startAnimation(transalteToTop)
        subtitle.startAnimation(transalteToTop)
        startContinue.startAnimation(btnAnim)


        startContinue.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, WelcomeTwoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
