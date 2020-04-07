package com.zerone.zerone_pos.ui.history
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zerone.zerone_pos.repository.TransactionRepository

class HistoryViewModelFactory (
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java))
            return HistoryViewModel(transactionRepository) as T
        throw IllegalArgumentException()
    }
}