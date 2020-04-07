package com.zerone.zerone_pos.datastore.kategori

import com.zerone.zerone_pos.database.KategoriDao
import com.zerone.zerone_pos.ui.kategori.KategoriModel

class KategoriRoomDataStore (private val kategoriDao: KategoriDao) : KategoriDataStore {
    override suspend fun getKategori(keyword: String): MutableList<KategoriModel>? {
        val response = kategoriDao.getAll(keyword)
        return if (response.isEmpty()) null else response
    }

    override suspend fun addAll(set: String, pokemons: MutableList<KategoriModel>?) {
        pokemons?.let { kategoriDao.insertAll(*it.toTypedArray()) }
    }
}