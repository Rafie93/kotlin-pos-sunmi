package com.zerone.zerone_pos.datastore.produk


import android.util.Log
import com.zerone.zerone_pos.ui.produk.ProdukModel
import com.zerone.zerone_pos.webservice.ZeronePosService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

class ProdukRemoteDataStore (private val zeronePosService: ZeronePosService,val auth: String) : ProdukDataStore {
    override suspend fun getProduks(kategori: String,keyword:String): MutableList<ProdukModel>? {
        Log.d("TOKEN",auth)
        val response = zeronePosService.getProduk(auth!!,kategori,keyword)
        if (response.isSuccessful) return response.body()?.data
        throw Exception("Terjadi kesalahan saat melakukan request data, status error ${response.code()}")
    }

    override suspend fun addAll(set: String, produks: MutableList<ProdukModel>?) {

    }

    override fun addSingle(
        code: String,
        name: String,
        basicPrice: String,
        sellingPrice: String,
        isStok: Int,
        unit: String,
        category: String,
        stock: String,
        deskripsi:String
    ) {
        zeronePosService.storeProduct(auth!!,code,name,basicPrice,sellingPrice,isStok,unit,category,stock,deskripsi)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
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

    override fun addSingleWithImage(body: MultipartBody.Part, map: HashMap<String, RequestBody>) {
        zeronePosService.storeProductWithImage(auth!!,body,map)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
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

    override suspend fun deleteProduk(id: String) {
        Log.d("ID",id)
        zeronePosService.deleteProduk(auth,id)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("DELETE",t.message)
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful){
                        Log.d("DELETE","sukses")
                    }else{
                        Log.d("DELETE",response.message())
                    }
                }
            })
    }

    override fun updateProduk(
        code: String,
        name: String,
        basicPrice: String,
        sellingPrice: String,
        isStok: Int,
        unit: String,
        category: String,
        stock: String,
        deskripsi: String,
        id: String
    ) {
        zeronePosService.updateProduct(auth!!,code,name,basicPrice,sellingPrice,isStok,unit,category,stock,deskripsi,id)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("UPDATE_ERROR",t.message)
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful){
                        Log.d("UPDATE_SUKSES","sukess update")
                    }else{
                        Log.d("UPDATE_GAGAL",response.message())
                    }
                }
            })
    }

    override fun updateWithImage(
        id: String,
        body: MultipartBody.Part,
        map: HashMap<String, RequestBody>
    ) {
        zeronePosService.updateProductWithImage(auth!!,id,body,map)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
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
