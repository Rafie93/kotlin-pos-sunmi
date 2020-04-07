package com.zerone.zerone_pos.ui.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zerone.zerone_pos.R
import kotlinx.android.synthetic.main.activity_change_password.*
import org.jetbrains.anko.indeterminateProgressDialog

class ChangePasswordActivity : AppCompatActivity() {
    private val viewModel: AccountViewModel by lazy { ViewModelProvider(this).get(AccountViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnUpdate.setOnClickListener(View.OnClickListener {
            val passwordOld = editTextPasswordOld.text.toString().trim()
            val passwordNew = editTextPasswordNew.text.toString().trim()
            val passwordConfirm = editTextPasswordConfirm.text.toString().trim()

            if(passwordOld.isEmpty()){
                editTextPasswordOld.error = getString(R.string.passwordOld)+" "+getString(R.string.required)
                editTextPasswordOld.requestFocus()
                return@OnClickListener
            }
            if(passwordNew.isEmpty()){
                editTextPasswordNew.error = getString(R.string.passwordNew)+" "+getString(R.string.required)
                editTextPasswordNew.requestFocus()
                return@OnClickListener
            }
            if (passwordConfirm!=passwordNew){
                editTextPasswordConfirm.error = getString(R.string.password_not_same)
                editTextPasswordConfirm.requestFocus()
                return@OnClickListener
            }
            val dialog = indeterminateProgressDialog(message = getString(R.string.please_wait), title = "")
            dialog.show()
            viewModel.changePassword(passwordOld,passwordNew,passwordConfirm)
            with(viewModel) {
                showToast.observe(this@ChangePasswordActivity, Observer {
                    dialog.dismiss()
                    Toast.makeText(this@ChangePasswordActivity,"$it", Toast.LENGTH_LONG).show()
                })

                error.observe(this@ChangePasswordActivity, Observer {
                   Toast.makeText(this@ChangePasswordActivity,"$it",Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                })

            }


        })


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
