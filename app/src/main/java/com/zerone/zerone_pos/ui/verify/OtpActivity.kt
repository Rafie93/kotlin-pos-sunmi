package com.zerone.zerone_pos.ui.verify
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_otp.*


class OtpActivity : AppCompatActivity() {
    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable
    var email =""
    val maxResend = 300
    var hitungMundur = maxResend
    var timeIncrement = 0
    var stopIncement = false
    private val viewModel: OtpViewModel by lazy { ViewModelProvider(this).get(OtpViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_otp)

        var bundle :Bundle ?=intent.extras
        email = bundle!!.getString("email")!!
        val isRegistrasi = bundle!!.getBoolean("registrasi")
        if (isRegistrasi) resendOtpToEmail(email)

        resendOtp.setOnClickListener(View.OnClickListener {
            stopIncement = false
            timeIncrement = 0
            hitungMundur = maxResend
            resendOtp.setBackgroundResource(R.drawable.button_round_background_disable)
            resendOtp.isEnabled = false
            resendOtpToEmail(email)
            countdown()
        })

        mHandler = Handler()
        mRunnable = Runnable {
            if (!stopIncement){
                countdown()
            }
            verifyOtp()
            mHandler.postDelayed(
                mRunnable,
                1000
            )
        }
        mHandler.postDelayed(
            mRunnable,
            1000
        )
    }

    fun countdown() {
          hitungMundur = maxResend - timeIncrement
         if (hitungMundur == 0){
             stopIncement = true
             timeIncrement = 0
             resendOtp.setBackgroundResource(R.drawable.button_round_background)
             resendOtp.isEnabled = true
         }
         var textCoundown = "RESEND IN ("+hitungMundur.toString()+")"
         resendOtp.setText(textCoundown)
         timeIncrement++

     }

    fun resendOtpToEmail(myEmail:String) {
        viewModel.otpResend(myEmail)
        with(viewModel) {
            showToast.observe(this@OtpActivity, Observer {
                Toast.makeText(applicationContext,"$it",Toast.LENGTH_SHORT).show()
            })
        }
    }


    fun verifyOtp(){
        val h1 = editOtp1.text.toString().trim()
        val h2 = editOtp2.text.toString().trim()
        val h3 = editOtp3.text.toString().trim()
        val h4 = editOtp4.text.toString().trim()

        if (hitungMundur!=0) {
            if (!h1.isEmpty() && !h2.isEmpty() && !h3.isEmpty() && !h4.isEmpty()) {
                val code = h1 + h2 + h3 + h4
                viewModel.otpVerify(code,email)
                with(viewModel) {
                    isSuccessVerify.observe(this@OtpActivity, Observer {
                        if (it){
                            Toast.makeText(applicationContext,"Verification successful",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@OtpActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
                }
            }
        }
    }
}
