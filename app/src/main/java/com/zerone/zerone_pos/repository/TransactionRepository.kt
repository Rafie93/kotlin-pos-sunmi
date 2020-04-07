package com.zerone.zerone_pos.repository

import android.util.Log
import com.zerone.zerone_pos.datastore.transaction.TransactionDataStore
import com.zerone.zerone_pos.helpers.TransactionHelper
import com.zerone.zerone_pos.ui.transaction.TransactionModel

class TransactionRepository private constructor() : BaseRepository<TransactionDataStore>() {
    suspend fun getTransaction(invoice:String): MutableList<TransactionModel>? {
        val cache = localDataStore?.getTransaction(invoice)
        if (!cache.toString().equals("null")) return cache
        val response = remoteDataStore?.getTransaction(invoice)
        if (cache == response){
            localDataStore?.addAll(invoice,response)
            return response
        }else {
            return response
        }
    }

    suspend fun addTransaction( invoice:String,date:String,time:String,total_product:String,total_sales:String,
                                additional:String,discount:String,tax:String,grand_total:String,pay:String,
                                change:String,notes:String,product_transaction:String
    ): MutableList<TransactionModel>? {

        remoteDataStore?.addSingle(invoice,date,time,total_product,total_sales,additional,discount,tax,grand_total,pay,
            change,notes,product_transaction)
        val newResponse = remoteDataStore?.getTransaction(invoice)
        val response = remoteDataStore?.getTransaction(invoice)
        localDataStore?.addAll(invoice,response)

        Log.d("TRANSACTION",newResponse.toString())
        return newResponse
    }

    companion object {
        val instance by lazy { TransactionRepository() }
    }
}