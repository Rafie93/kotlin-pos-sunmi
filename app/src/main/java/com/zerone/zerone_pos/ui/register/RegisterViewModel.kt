package com.zerone.zerone_pos.ui.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonArray
import com.zerone.zerone_pos.helpers.SingleLiveEvent
import com.zerone.zerone_pos.webservice.NetworkApp
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(app: Application
) : AndroidViewModel(app) {
    val error : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val registerError :  SingleLiveEvent<JSONArray> by lazy { SingleLiveEvent<JSONArray>() }
    val success : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun register(name:String, email:String,phone:String,password:String,passwordConfirm: String,
                      merchantName:String,merchantBusiness:String,merchantAddress:String,merchantCity:String) {
        NetworkApp.api().register(name,email,phone,password,passwordConfirm,
            merchantName, merchantBusiness,merchantAddress,merchantCity).enqueue(object :
            Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                success.postValue(false)
                error.postValue(t!!.message)
            }

            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    success.postValue(true)
                } else {
                    success.postValue(false)
                    val jsonRESULTS = JSONObject(response.errorBody()!!.string())
                    val jsonArray = jsonRESULTS.getJSONArray("errors")
                    registerError.postValue(jsonArray)
                }
            }
        })
    }
}

