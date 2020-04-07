package com.zerone.zerone_pos.ui.verify
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zerone.zerone_pos.helpers.SingleLiveEvent
import com.zerone.zerone_pos.webservice.NetworkApp
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OtpViewModel(app: Application
) : AndroidViewModel(app) {
    val context =app
    val isSuccessVerify : SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }
    val showToast : SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }

    fun otpVerify(code:String,email:String) {
        NetworkApp.api().otpVerify(code,email).enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                isSuccessVerify.postValue(false)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    isSuccessVerify.postValue(true)
                } else {
                    isSuccessVerify.postValue(false)
                }
            }

        })
    }
    fun otpResend(email: String){
        NetworkApp.api().otpResend(email).enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showToast.postValue(t.message)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    var jsonRESULTS: JSONObject? = null
                    jsonRESULTS = JSONObject(response.body()!!.string())
                    val message = jsonRESULTS.get("message").toString()
                    showToast.postValue(message)
                } else {
                    var jsonRESULTS: JSONObject? = null
                    jsonRESULTS = JSONObject(response.errorBody()!!.string())
                    val message = jsonRESULTS.get("message").toString()
                    showToast.postValue(message)
                }
            }

        })
    }
}
