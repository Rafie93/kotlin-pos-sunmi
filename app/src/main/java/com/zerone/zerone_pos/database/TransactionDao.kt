package com.zerone.zerone_pos.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zerone.zerone_pos.ui.transaction.TransactionModel

@Dao
interface TransactionDao {
    @Query("SELECT * FROM sale WHERE 1=1 ")
    suspend fun getAll(): MutableList<TransactionModel>

    @Query("SELECT id FROM sale ORDER BY id DESC LIMIT 1 ")
    fun getLast():Int

    @Query("DELETE FROM sale")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg transactions: TransactionModel)


    @Query("INSERT INTO sale values(:id,:invoice,:date,:time,:total_product,:total_sales,:tax,:additional,:discount,:grand_total,:pay,:change,:notes,:product_transaction)")
     fun insertSingle(id:Int,invoice:String,date:String,time:String,
                      total_product: String,
                      total_sales: String,
                      additional: String,
                      discount: String,
                      tax: String,
                      grand_total: String,
                      pay: String,
                      change: String,
                      notes: String,
                      product_transaction: String)

}