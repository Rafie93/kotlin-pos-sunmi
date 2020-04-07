package com.zerone.zerone_pos.datastore.produk

import com.zerone.zerone_pos.ui.produk.ProdukModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap

class ProdukLocalDataStore : ProdukDataStore{
    private val caches = mutableMapOf<String, MutableList<ProdukModel>?>()

    override suspend fun getProduks(kategori: String,keyword:String): MutableList<ProdukModel>? =
        if (caches.contains(keyword)) caches[keyword] else null

    override suspend fun addAll(set: String, produks: MutableList<ProdukModel>?) {
        caches[set] = produks
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
    }

    override fun addSingleWithImage(body: MultipartBody.Part, map: HashMap<String, RequestBody>) {

    }

    override suspend fun deleteProduk(id: String) {

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

    }

    override fun updateWithImage(
        id: String,
        body: MultipartBody.Part,
        map: HashMap<String, RequestBody>
    ) {

    }
}