package com.zerone.zerone_pos.ui.account

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zerone.zerone_pos.helpers.Const
import com.zerone.zerone_pos.helpers.SharedPrefControl
import com.zerone.zerone_pos.helpers.SingleLiveEvent
import com.zerone.zerone_pos.webservice.NetworkApp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccountViewModel(app: Application
) : AndroidViewModel(app) {
    val context =app
    val auth = SharedPrefControl.getInstance(context.applicationContext).getToken()
    val error : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val showToast : SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    val isSukses : SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }

    fun changePassword(passwordOld:String, passwordNew:String,passwordConfirm:String) {
        NetworkApp.api().changePassword(auth!!,passwordOld,passwordNew,passwordConfirm).enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                error.postValue(t!!.message)
                isSukses.postValue(false)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    var jsonRESULTS: JSONObject? = null
                    jsonRESULTS = JSONObject(response.body()!!.string())
                    val message = jsonRESULTS.get("message").toString()
                    showToast.postValue(message)
                    isSukses.postValue(true)
                } else {
                    var jsonRESULTS: JSONObject? = null
                    jsonRESULTS = JSONObject(response.errorBody()!!.string())
                    val message = jsonRESULTS.get("message").toString()
                    showToast.postValue(message)
                    isSukses.postValue(false)
                }
            }
        })
    }

    fun changeProfile(name:String,phone:String){
        NetworkApp.api().changeProfile(auth!!,name,phone).enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                error.postValue(t!!.message)
                isSukses.postValue(false)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    var jsonRESULTS: JSONObject? = null
                    jsonRESULTS = JSONObject(response.body()!!.string())
                    val message = jsonRESULTS.get("message").toString()
                    val dataObject = jsonRESULTS.getJSONObject("data")
                    val name = dataObject.get("name").toString()
                    val phone = dataObject.get("phone").toString()
                    val image = dataObject.get("image").toString()
                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.USER_NAMA,name)
                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.USER_TELP,phone)
                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.USER_IMAGE,image)
                    showToast.postValue(message)
                    isSukses.postValue(true)

                } else {
                    var jsonRESULTS: JSONObject? = null
                    jsonRESULTS = JSONObject(response.errorBody()!!.string())
                    val message = jsonRESULTS.get("message").toString()
                    showToast.postValue(message)
                    isSukses.postValue(false)

                }
            }
        })
    }

    fun changeProfileWithImage(body: MultipartBody.Part, map: HashMap<String, RequestBody>) {
        NetworkApp.api().changeProfileWithImage(auth!!,body,map).enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                error.postValue(t!!.message)
                isSukses.postValue(false)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    var jsonRESULTS: JSONObject? = null
                    jsonRESULTS = JSONObject(response.body()!!.string())
                    val message = jsonRESULTS.get("message").toString()
                    val dataObject = jsonRESULTS.getJSONObject("data")
                    val name = dataObject.get("name").toString()
                    val phone = dataObject.get("phone").toString()
                    val image = dataObject.get("image").toString()
                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.USER_NAMA,name)
                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.USER_TELP,phone)
                    SharedPrefControl.getInstance(context.applicationContext).saveString(Const.USER_IMAGE,image)
                    showToast.postValue(message)
                    isSukses.postValue(true)
                } else {
                    var jsonRESULTS: JSONObject? = null
                    jsonRESULTS = JSONObject(response.errorBody()!!.string())
                    val message = jsonRESULTS.get("message").toString()
                    showToast.postValue(message)
                    isSukses.postValue(false)

                }
            }
        })
    }

}

