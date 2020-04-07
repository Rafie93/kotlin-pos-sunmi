package com.zerone.zerone_pos.ui.home
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zerone.zerone_pos.helpers.SharedPrefControl
import com.zerone.zerone_pos.helpers.SingleLiveEvent
import com.zerone.zerone_pos.webservice.NetworkApp
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(app: Application
) : AndroidViewModel(app){
    val context =app
    val sales_total : SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>() }
    val sales_day : SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>() }
    val sales_month : SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>() }
    val home_response_data: MutableLiveData<HomeModel> by lazy { MutableLiveData<HomeModel>() }
    val access_danied : SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }
    var errorLoad = 0
    init {
        getDashboard()
    }

    fun getDashboard() {
        val token = SharedPrefControl.getInstance(context.applicationContext).getToken()
        NetworkApp.api().dashboard(token!!).enqueue(object :
            Callback<HomeModel> {
            override fun onFailure(call: Call<HomeModel>, t: Throwable) {
                if (errorLoad < 2){
                    getDashboard()
                    errorLoad++
                }
            }

            override fun onResponse(call: Call<HomeModel>, response: Response<HomeModel>) {
                if (response.isSuccessful) {
                    val salesTotal = response.body()!!.sales_total
                    val salesDay = response.body()!!.sales_day
                    val salesMonth = response.body()!!.sales_month
                    sales_total.postValue(salesTotal)
                    sales_day.postValue(salesDay)
                    sales_month.postValue(salesMonth)
                    home_response_data.postValue(response.body())
                }else if (response.code() == 401){ // NOT ACCESS FROM API
                    access_danied.postValue(true)
                }
            }

        })
    }
}