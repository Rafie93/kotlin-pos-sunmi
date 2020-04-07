package com.zerone.zerone_pos.datastore.produk

import com.zerone.zerone_pos.ui.produk.ProdukModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap

interface ProdukDataStore {
    suspend fun getProduks(kategori: String,keyword:String): MutableList<ProdukModel>?
    suspend fun addAll(keyword: String, produks: MutableList<ProdukModel>?)
    fun addSingle(code:String,name:String,
                  basicPrice:String,sellingPrice:String,
                  isStok:Int,unit:String,
                  category:String,stock:String,
                  deskripsi:String)

    fun addSingleWithImage(
        body: MultipartBody.Part,
        map: HashMap<String, RequestBody>
    )

    suspend fun deleteProduk(id: String)

    fun updateProduk(code:String,name:String,
                  basicPrice:String,sellingPrice:String,
                  isStok:Int,unit:String,
                  category:String,stock:String,
                  deskripsi:String, id: String)

    fun updateWithImage(
        id:String,
        body: MultipartBody.Part,
        map: HashMap<String, RequestBody>
    )

}