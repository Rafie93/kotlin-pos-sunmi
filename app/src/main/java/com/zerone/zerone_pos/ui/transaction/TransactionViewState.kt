package com.zerone.zerone_pos.ui.transaction

data class TransactionViewState (
    val loading: Boolean = false,
    val error: Exception? = null,
    val data: MutableList<TransactionModel>? = null
)
