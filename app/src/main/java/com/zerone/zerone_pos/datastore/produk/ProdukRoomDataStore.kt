package com.zerone.zerone_pos.datastore.produk

import com.zerone.zerone_pos.database.ProdukDao
import com.zerone.zerone_pos.ui.produk.ProdukModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap

class ProdukRoomDataStore (private val produkDao: ProdukDao) : ProdukDataStore {
    override suspend fun getProduks(kategori: String,keyword:String): MutableList<ProdukModel>? {
        if (!keyword.equals("")){
            val response = produkDao.getByCodeOrName(keyword)
            return if (response.isEmpty()) null else response
        }
        val response = produkDao.getAll()
        return if (response.isEmpty()) null else response
    }

    override suspend fun addAll(set: String, produk: MutableList<ProdukModel>?) {
        produk?.let { produkDao.insertAll(*it.toTypedArray()) }
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
        produkDao.deleteId(id)
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