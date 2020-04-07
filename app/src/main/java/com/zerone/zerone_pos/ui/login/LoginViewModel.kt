package com.zerone.zerone_pos.ui.login

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zerone.zerone_pos.helpers.Const
import com.zerone.zerone_pos.helpers.SharedPrefControl
import com.zerone.zerone_pos.helpers.SingleLiveEvent
import com.zerone.zerone_pos.webservice.NetworkApp
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(app: Application
) : AndroidViewModel(app) {
    val context =app
    val error : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val showToast : SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    val isLogin : SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }
    val showFormVerify : SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }

    fun validasiLogin(email:String, password:String) {
        NetworkApp.api().login(email,password).enqueue(object :
            Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                error.postValue(t!!.message)
                Log.d("FAILED","Ini FAILED")
                isLogin.postValue(false)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    SharedPrefControl.getInstance(context.applicationContext).saveToken(response.body()!!.token)
                    SharedPrefControl.getInstance(context.applicationContext).sLogin(true)
                    val userId = response.body()!!.data.id
                    val userName = response.body()!!.data.name
                    val userPhone = response.body()!!.data.phone
                    val userEmail = response.body()!!.data.email
                    val userImage = response.body()!!.data.image
                    val userMerchantName = response.body()!!.data.merchant_name
                    val userMerchantCity = response.body()!!.data.merchant_city
                    val userMerchantAddress = response.body()!!.data.merchant_address
                    val userMerchantLogo = response.body()!!.data.merchant_logo

                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.USER_ID,userId.toString())
                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.USER_NAMA,userName)
                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.USER_TELP,userPhone)
                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.USER_EMAIL,userEmail)
                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.USER_IMAGE,userImage!!)

                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.MERCHANT_NAME,userMerchantName)
                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.MERCHANT_CITY,userMerchantCity!!)
                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.MERCHANT_ADDRESS,userMerchantAddress!!)
                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.MERCHANT_LOGO,userMerchantLogo!!)

                    showToast.postValue("Sukses Your Login")
                    isLogin.postValue(true)
                } else {
                    Log.d("BAD","Ini ERROR")
                    var jsonRESULTS: JSONObject? = null
                    jsonRESULTS = JSONObject(response.errorBody()!!.string())
                    val message = jsonRESULTS.get("message").toString()
                    showToast.postValue(message)
                    isLogin.postValue(false)
                    if (response.code()==422){
                        showFormVerify.postValue(true)
                    }
                }
            }

        })
    }
}

