package com.zerone.zerone_pos.datastore.transaction

import android.util.Log
import com.zerone.zerone_pos.ui.produk.ProdukModel
import com.zerone.zerone_pos.ui.transaction.TransactionModel
import com.zerone.zerone_pos.webservice.ZeronePosService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class TransactionRemoteDataStore  (private val zeronePosService: ZeronePosService, val auth: String) : TransactionDataStore {
    override suspend fun getTransaction(invoice:String): MutableList<TransactionModel>? {
        val response = zeronePosService.getTransaction(auth!!,invoice)
        if (response.isSuccessful) return response.body()?.data
        throw Exception("Terjadi kesalahan saat melakukan request data, status error ${response.code()}")
    }

    override suspend fun addAll(invoice: String, transactions: MutableList<TransactionModel>?) {

    }

    override fun addSingle(
        invoice: String,
        date: String,
        time: String,
        total_product: String,
        total_sales: String,
        additional: String,
        discount: String,
        tax: String,
        grand_total: String,
        pay: String,
        change: String,
        notes: String,
        product_transaction: String
    ) {
        zeronePosService.storeTransaction(auth!!,invoice,date,time,total_product,
            total_sales,additional,tax,discount,grand_total,pay,change,notes,product_transaction)
            .enqueue(object : retrofit2.Callback< ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("ERRORNYA",t.message)
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful){
                        Log.d("STORE","sukess transaction")
                    }else{
                        Log.d("GAGAL",response.message())
                    }
                }
            })
    }
}
