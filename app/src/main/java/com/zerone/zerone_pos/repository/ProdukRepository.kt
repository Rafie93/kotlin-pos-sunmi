package com.zerone.zerone_pos.repository

import android.util.Log
import com.zerone.zerone_pos.datastore.produk.ProdukDataStore
import com.zerone.zerone_pos.ui.produk.ProdukModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap

class ProdukRepository private constructor() : BaseRepository<ProdukDataStore>() {
    suspend fun getProduks(kategori: String,keyword: String): MutableList<ProdukModel>? {
        val cache = localDataStore?.getProduks(kategori,keyword)
        Log.d("CHACE_PRODUK",cache.toString())
        if (!cache.toString().equals("null") ) return cache
        val response = remoteDataStore?.getProduks(kategori,keyword)
        if (cache != response){
            localDataStore?.addAll(keyword, response)
            return response
        }else{
            return cache
        }
    }

    suspend fun addSingle(code:String,name:String,
                          basicPrice:String,sellingPrice:String,
                          isStok:Int,unit:String,
                          category:String,stock:String,
                          desripsi:String
    ): MutableList<ProdukModel>? {

        remoteDataStore?.addSingle(code,name,basicPrice,sellingPrice,isStok,unit,category,stock,desripsi)
        val newResponse = remoteDataStore?.getProduks("all","")
        val response = remoteDataStore?.getProduks("all","")
        localDataStore?.addAll("all",response)
        return newResponse
    }

    suspend fun addSingleWithImage(
        body: MultipartBody.Part,
        map: HashMap<String, RequestBody>
    ): MutableList<ProdukModel>? {

        remoteDataStore?.addSingleWithImage(body,map)
        val newResponse = remoteDataStore?.getProduks("all","")
        val response = remoteDataStore?.getProduks("all","")
        localDataStore?.addAll("all",response)
        return newResponse
    }

    suspend fun deleteProduk(id:String): MutableList<ProdukModel>? {

        remoteDataStore?.deleteProduk(id)
        val newResponse = remoteDataStore?.getProduks("all","")
        localDataStore?.deleteProduk(id)
        val response = remoteDataStore?.getProduks("all","")
        localDataStore?.addAll("all",response)
        return newResponse
    }

    suspend fun updateProduk(code:String,name:String,
                          basicPrice:String,sellingPrice:String,
                          isStok:Int,unit:String,
                          category:String,stock:String,
                          desripsi:String,id: String
    ): MutableList<ProdukModel>? {

        remoteDataStore?.updateProduk(code,name,basicPrice,sellingPrice,isStok,unit,category,stock,desripsi,id)
        val newResponse = remoteDataStore?.getProduks("all","")
        val response = remoteDataStore?.getProduks("all","")
        localDataStore?.addAll("all",response)
        return newResponse
    }

    suspend fun updateWithImage(
        id: String,
        body: MultipartBody.Part,
        map: HashMap<String, RequestBody>
    ): MutableList<ProdukModel>? {

        remoteDataStore?.updateWithImage(id,body,map)
        val newResponse = remoteDataStore?.getProduks("all","")
        val response = remoteDataStore?.getProduks("all","")
        localDataStore?.addAll("all",response)
        return newResponse
    }

    companion object {
        val instance by lazy { ProdukRepository() }
    }
}