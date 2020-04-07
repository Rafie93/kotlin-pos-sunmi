package com.zerone.zerone_pos.ui.produk
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.repository.ProdukRepository
import kotlinx.android.synthetic.main.activity_produk.*

class ProdukActivity : AppCompatActivity() {
    private lateinit var vm: ProdukViewModel
    private lateinit var adapter: ProdukAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produk)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        addNewProduk.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, NewProdukActivity::class.java)
            startActivity(intent)
        })

        adapter = ProdukAdapter()
        recyclerView.adapter = adapter

        val factory = ProdukViewModelFactory(ProdukRepository.instance)
        vm = ViewModelProvider(this,factory).get(ProdukViewModel::class.java).apply {
            viewState.observe(
                this@ProdukActivity,
                Observer(this@ProdukActivity::handleState)
            )
            if (viewState.value?.data == null) getProduks("all","")
            swiptToRefresh.setOnRefreshListener { getProduks("all","") }
            searchText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    getProduks("all",query)
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    getProduks("all",newText)
                    return false
                }
            })

        }
    }

    private fun handleState(viewState: ProdukViewState?) {
        viewState?.let {
            toggleLoading(it.loading)
            it.data?.let { data -> showData(data) }
            it.error?.let { error -> showError(error) }
        }
    }

    private fun showData(data: MutableList<ProdukModel>) {
        adapter.updateData(data)
        recyclerView.visibility = View.VISIBLE
    }

    private fun showError(error: Exception) {
        error.printStackTrace()
        recyclerView.visibility = View.GONE
    }

    private fun toggleLoading(loading: Boolean) {
        swiptToRefresh.isRefreshing = loading
    }

}
