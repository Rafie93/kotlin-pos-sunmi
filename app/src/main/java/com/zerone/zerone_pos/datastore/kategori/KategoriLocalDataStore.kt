package com.zerone.zerone_pos.datastore.kategori

import com.zerone.zerone_pos.ui.kategori.KategoriModel

class KategoriLocalDataStore : KategoriDataStore{
    private val caches = mutableMapOf<String, MutableList<KategoriModel>?>()

    override suspend fun getKategori(keyword: String): MutableList<KategoriModel>? =
        if (caches.contains(keyword)) caches[keyword] else null

    override suspend fun addAll(set: String, kategoris: MutableList<KategoriModel>?) {
        caches[set] = kategoris
    }

}