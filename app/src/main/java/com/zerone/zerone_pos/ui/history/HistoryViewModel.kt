package com.zerone.zerone_pos.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zerone.zerone_pos.ui.transaction.TransactionViewState


import android.util.Log
import androidx.lifecycle.viewModelScope
import com.zerone.zerone_pos.repository.TransactionRepository
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val mViewState = MutableLiveData<TransactionViewState>().apply {
        value = TransactionViewState(loading = true)
    }
    val viewState: LiveData<TransactionViewState>
        get() = mViewState

    fun getTransactions(invoice: String) = viewModelScope.launch {
        try {
            val data = transactionRepository.getTransaction(invoice)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data)
        } catch (ex: Exception) {
            mViewState.value = mViewState.value?.copy(loading = false, error = ex, data = null)
        }
    }
}