package com.zerone.zerone_pos.datastore.kategori

import com.zerone.zerone_pos.ui.kategori.KategoriModel
import com.zerone.zerone_pos.webservice.ZeronePosService

class KategoriRemoteDataStore (private val zeronePosService: ZeronePosService,val auth: String) : KategoriDataStore {
    override suspend fun getKategori(keyword: String): MutableList<KategoriModel>? {
        val response = zeronePosService.getCategory(auth,keyword)
        if (response.isSuccessful) return response.body()?.data
        throw Exception("Terjadi kesalahan saat melakukan request data, status error ${response.code()}")
    }

    override suspend fun addAll(set: String, produks: MutableList<KategoriModel>?) {

    }
}
