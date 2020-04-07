package com.zerone.zerone_pos.datastore.transaction

import com.zerone.zerone_pos.ui.transaction.TransactionModel

class TransactionLocalDataStore : TransactionDataStore{
    private val caches = mutableMapOf<String, MutableList<TransactionModel>?>()

    override suspend fun getTransaction(invoice: String): MutableList<TransactionModel>? =
        if (caches.contains(invoice)) caches[invoice] else null

    override suspend fun addAll(invoice: String, transactins: MutableList<TransactionModel>?) {
        caches[invoice] = transactins
    }

    override  fun addSingle(
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

    }


}