package com.zerone.zerone_pos.datastore.transaction

import com.zerone.zerone_pos.ui.transaction.TransactionModel

interface TransactionDataStore {
    suspend fun getTransaction(invoice:String): MutableList<TransactionModel>?
    suspend fun addAll(invoice:String,transactions: MutableList<TransactionModel>?)
     fun addSingle(invoice:String,date:String,time:String,total_product:String,total_sales:String,
                          additional:String,discount:String,tax:String,grand_total:String,pay:String,
                          change:String,notes:String,product_transaction:String)

}