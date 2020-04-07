package com.zerone.zerone_pos.database

import androidx.room.*
import com.zerone.zerone_pos.ui.produk.ProdukModel

@Dao
interface ProdukDao {
    @Query("SELECT * FROM produk WHERE 1=1 ")
    suspend fun getAll(): MutableList<ProdukModel>

    @Query("SELECT * FROM produk WHERE 1=1 AND (code LIKE :keyword OR name LIKE :keyword) ")
    suspend fun getByCodeOrName(keyword:String): MutableList<ProdukModel>

    @Query("DELETE FROM produk")
     fun deleteAll()

    @Query("DELETE FROM produk WHERE id=:id")
    suspend fun deleteId(id:String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg produks: ProdukModel)
}