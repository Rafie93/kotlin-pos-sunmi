package com.zerone.zerone_pos.ui.register
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.ui.login.LoginActivity
import com.zerone.zerone_pos.ui.verify.OtpActivity
import kotlinx.android.synthetic.main.activity_new_produk.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONArray

class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by lazy { ViewModelProvider(this).get(RegisterViewModel::class.java) }

    var business_type="Lain-Lain"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val arrBusiness = resources.getStringArray(R.array.arrBusinessType)
        val adapterBusiness = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrBusiness)
        chooseBusiness.adapter = adapterBusiness
        chooseBusiness.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                business_type = arrBusiness[position].toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        readyAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerNow.setOnClickListener(View.OnClickListener {
            val merchantName = editTextMerchantName.text.toString().trim()
            val merchantCity = editTextMerchantCity.text.toString().trim()
            val merchantAddress = editTextMerchantAddress.text.toString().trim()
            val name = editTextFullname.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()

            val password = editTextPassword.text.toString().trim()
            val passwordConfirm = editTextPasswordConfirm.text.toString().trim()

            if(merchantName.isEmpty()){
                editTextMerchantName.error = getString(R.string.merchant_name) +" "+getString(R.string.required)
                editTextMerchantName.requestFocus()
                return@OnClickListener
            }
            if(merchantCity.isEmpty()){
                editTextMerchantCity.error =  getString(R.string.merchant_city)+" "+getString(R.string.required)
                editTextMerchantCity.requestFocus()
                return@OnClickListener
            }
            if(merchantAddress.isEmpty()){
                editTextMerchantAddress.error =  getString(R.string.merchant_address)+" "+getString(R.string.required)
                editTextMerchantAddress.requestFocus()
                return@OnClickListener
            }
            if(name.isEmpty()){
                editTextFullname.error =  getString(R.string.name_hint)+" "+getString(R.string.required)
                editTextFullname.requestFocus()
                return@OnClickListener
            }
            if(email.isEmpty()){
                editTextEmail.error = "Email "+getString(R.string.required)
                editTextEmail.requestFocus()
                return@OnClickListener
            }
            if(phone.isEmpty()){
                editTextPhone.error = "Phone "+getString(R.string.required)
                editTextPhone.requestFocus()
                return@OnClickListener
            }
            if(password.isEmpty()){
                editTextPassword.error = "Password "+getString(R.string.required)
                editTextPassword.requestFocus()
                return@OnClickListener
            }
            if(password != passwordConfirm){
                editTextPasswordConfirm.error = getString(R.string.password_not_same)
                editTextPasswordConfirm.requestFocus()
                return@OnClickListener
            }

            viewModel.register(name,email,phone,password,passwordConfirm,merchantName,business_type,merchantAddress,merchantCity)
            with(viewModel) {
                success.observe(this@RegisterActivity, Observer {
                    if (it){
                        val intent = Intent(this@RegisterActivity, OtpActivity::class.java)
                        intent.putExtra("email",email)
                        intent.putExtra("registrasi",true)
                        startActivity(intent)
                    }
                })
                registerError.observe(this@RegisterActivity, Observer {
                   handleError(it)
                })
                error.observe(this@RegisterActivity, Observer {
                    Toast.makeText(this@RegisterActivity,it,Toast.LENGTH_SHORT).show()
                })
            }
        })

    }

     fun handleError(it: JSONArray?) {
         for (i in 0 until it!!.length()){
             val mJsonObjectError = it.getJSONObject(i)
             val atribut = mJsonObjectError.getString("attribute")
             val message = mJsonObjectError.getString("message")
             if(atribut.equals("email")) editTextEmail.error = message
             if(atribut.equals("email")) editTextEmail.error = message
             if(atribut.equals("phone")) editTextPhone.error = message
             if(atribut.equals("merchant_name")) editTextMerchantName.error = message
             if(atribut.equals("name")) editTextName.error = message
         }
    }
}
