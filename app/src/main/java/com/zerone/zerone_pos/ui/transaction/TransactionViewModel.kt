package com.zerone.zerone_pos.ui.transaction
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zerone.zerone_pos.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(
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

    fun addTransactions(
        invoice:String,date:String,time:String,total_product:String,total_sales:String,
        additional:String,discount:String,tax:String,grand_total:String,pay:String,
        change:String,notes:String,product_transaction:String
    ) = viewModelScope.launch {
        try {
            val data = transactionRepository.addTransaction(invoice,date,time,
                total_product,total_sales, additional, discount, tax, grand_total, pay, change, notes, product_transaction)
            Log.d("TEST_DATA",invoice)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data)
        } catch (ex: Exception) {
            mViewState.value = mViewState.value?.copy(loading = false, error = ex, data = null)
        }
    }


}