package com.zerone.zerone_pos.datastore.kategori

import com.zerone.zerone_pos.ui.kategori.KategoriModel

interface KategoriDataStore {
    suspend fun getKategori(keyword: String): MutableList<KategoriModel>?
    suspend fun addAll(set: String, produks: MutableList<KategoriModel>?)

}