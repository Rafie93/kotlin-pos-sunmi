package com.zerone.zerone_pos.repository

import com.zerone.zerone_pos.datastore.kategori.KategoriDataStore
import com.zerone.zerone_pos.ui.kategori.KategoriModel

class KategoriRepository private constructor() : BaseRepository<KategoriDataStore>() {
    suspend fun getProduks(keyword: String): MutableList<KategoriModel>? {
        val cache = localDataStore?.getKategori(keyword)
        if (cache != null) return cache
        val response = remoteDataStore?.getKategori(keyword)
        localDataStore?.addAll(keyword, response)
        return response
    }

    companion object {
        val instance by lazy { KategoriRepository() }
    }
}