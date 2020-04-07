package com.zerone.zerone_pos.ui.main
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.Const
import com.zerone.zerone_pos.helpers.SharedPrefControl
import com.zerone.zerone_pos.ui.login.LoginActivity
import com.zerone.zerone_pos.ui.walkthrough.WelcomeActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : AppCompatActivity() {
    lateinit var smallToBig: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        smallToBig = AnimationUtils.loadAnimation(this,R.anim.small_to_big)
        imageLogo.startAnimation(smallToBig)

        setLocal("in")

        Handler().postDelayed({
            //start main activity
            val isLogin = SharedPrefControl.getInstance(applicationContext).isLogin()
            val isNotFirstApp = SharedPrefControl.getInstance(applicationContext).getBoolean(Const.IS_NOTFIRSTAPP)
            if(!isNotFirstApp){
                val intent = Intent(applicationContext, WelcomeActivity::class.java)
                startActivity(intent)
            }else
            if (isLogin!!){
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }else{
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            finish()
        },3000)
    }

    fun setLocal(language: String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = getResources()
        val configuration = resources.getConfiguration()
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
    }
}
