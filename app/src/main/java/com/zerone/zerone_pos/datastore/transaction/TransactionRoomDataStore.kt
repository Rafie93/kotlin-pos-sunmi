package com.zerone.zerone_pos.datastore.transaction

import android.R.id
import com.zerone.zerone_pos.database.TransactionDao
import com.zerone.zerone_pos.ui.transaction.TransactionModel


class TransactionRoomDataStore (private val transactionDao: TransactionDao) : TransactionDataStore {
    override suspend fun getTransaction(invoice:String): MutableList<TransactionModel>? {
        val response = transactionDao.getAll()
        return if (response.isEmpty()) null else response
    }

    override suspend fun addAll(set: String, transaction: MutableList<TransactionModel>?) {
        transaction?.let { transactionDao.insertAll(*it.toTypedArray()) }
    }

    override fun addSingle(
        invoice: String,
        date: String,
        time: String,
        total_product: String,
        total_sales: String,
        additional: String,
        discount: String,
        tax: String,
        grand_total: String,
        pay: String,
        change: String,
        notes: String,
        product_transaction: String
    ) {
        val idLast = transactionDao.getLast()
       transactionDao.insertSingle(idLast,invoice,date,time,total_product,total_sales,additional,discount,tax,grand_total,pay,change,notes,product_transaction)
    }


}