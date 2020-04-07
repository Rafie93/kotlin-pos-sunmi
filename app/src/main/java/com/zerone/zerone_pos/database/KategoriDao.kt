package com.zerone.zerone_pos.database

import androidx.room.*
import com.zerone.zerone_pos.ui.kategori.KategoriModel

@Dao
interface KategoriDao {
    @Query("SELECT * FROM kategori WHERE `category` = :set")
    suspend fun getAll(set: String): MutableList<KategoriModel>

    @Query("DELETE FROM kategori")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg kategoris: KategoriModel)
}
