package com.zerone.zerone_pos.ui.history
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.repository.TransactionRepository
import com.zerone.zerone_pos.ui.transaction.TransactionModel
import com.zerone.zerone_pos.ui.transaction.TransactionViewState
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        return root
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = HistoryAdapter()
        recyclerView.adapter = adapter

        val factory = HistoryViewModelFactory(TransactionRepository.instance)
        historyViewModel = ViewModelProvider(this,factory).get(HistoryViewModel::class.java).apply {
            viewState.observe(
                this@HistoryFragment,
                Observer(this@HistoryFragment::handleState)
            )
            if (viewState.value?.data == null) getTransactions("")
            swiptToRefresh.setOnRefreshListener { getTransactions("") }

        }
    }

    private fun handleState(viewState: TransactionViewState?) {
        viewState?.let {
            toggleLoading(it.loading)
            it.data?.let { data -> showData(data) }
            it.error?.let { error -> showError(error) }
        }
    }

    private fun showData(data: MutableList<TransactionModel>) {
        if(data.size>0) {
            adapter.updateData(data)
            imageLoading.visibility = View.GONE
            layoutEmpty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }else{
            imageLoading.visibility = View.GONE
            recyclerView.visibility = View.GONE
            layoutEmpty.visibility = View.VISIBLE
        }
    }

    private fun showError(error: Exception) {
        error.printStackTrace()
        recyclerView.visibility = View.GONE
    }

    private fun toggleLoading(loading: Boolean) {
        imageLoading.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        layoutEmpty.visibility = View.GONE

    }

}
