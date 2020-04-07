package com.zerone.zerone_pos.ui.login

/**
    name : rafie
    website : wwww.zerone-bjm.com
    dibuat : april 2020
*/

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.database.AppDatabase
import com.zerone.zerone_pos.ui.main.MainActivity
import com.zerone.zerone_pos.ui.register.RegisterActivity
import com.zerone.zerone_pos.ui.verify.OtpActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.indeterminateProgressDialog


class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener(View.OnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if(email.isEmpty()){
                editTextEmail.error = "Email "+getString(R.string.required)
                editTextEmail.requestFocus()
                return@OnClickListener
            }
            if(password.isEmpty()){
                editTextPassword.error = "Password "+getString(R.string.required)
                editTextPassword.requestFocus()
                return@OnClickListener
            }

            val dialog = indeterminateProgressDialog(message = getString(R.string.please_wait), title = "")
            dialog.show()
            viewModel.validasiLogin(email,password)
            with(viewModel) {
                showToast.observe(this@LoginActivity, Observer {
                    Toast.makeText(this@LoginActivity,"$it", Toast.LENGTH_LONG).show()
                })
                isLogin.observe(this@LoginActivity, Observer {
                    dialog.dismiss()
                    if (it){
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                })
                error.observe(this@LoginActivity, Observer {
                    Toast.makeText(this@LoginActivity,"$it", Toast.LENGTH_LONG).show()

                })
                showFormVerify.observe(this@LoginActivity, Observer {
                    val intent = Intent(this@LoginActivity, OtpActivity::class.java)
                    intent.putExtra("email",email)
                    intent.putExtra("registrasi",false)
                    startActivity(intent)
                })
            }

        })
    }


}
