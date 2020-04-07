package com.zerone.zerone_pos.ui.transaction
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.Formatt
import com.zerone.zerone_pos.helpers.ShoppingCart
import com.zerone.zerone_pos.repository.ProdukRepository
import com.zerone.zerone_pos.ui.main.CaptureActivityPortrait
import com.zerone.zerone_pos.ui.produk.*
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_transaction.*

class TransactionActivity : AppCompatActivity() {

    private lateinit var vm: ProdukViewModel
    private lateinit var adapter: TransactionAdapter
    val factory = ProdukViewModelFactory(ProdukRepository.instance)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Paper.init(this)

        adapter = TransactionAdapter()
        recyclerView.adapter = adapter

        var total = ShoppingCart.getCart()
            .fold(0.toInt()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.priceItem!!.toInt()) }
        txtPrice.text = "Rp.${Formatt.intToRibuan(total)}"
        txtQuantity.text = ShoppingCart.getShoppingCartSize().toString()
        btnKosongi.visibility = View.VISIBLE

        getProduct("all","",false)

        scanCode.setOnClickListener(View.OnClickListener {
            run {
                IntentIntegrator(this@TransactionActivity)
                    .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
                    .setOrientationLocked(true)
                    .setBeepEnabled(true)
                    .setBarcodeImageEnabled(true)
                    .setCaptureActivity(CaptureActivityPortrait::class.java)
                    .initiateScan()
            }
        })

        btnKosongi.setOnClickListener {
            ShoppingCart.removeAll(applicationContext)
            txtPrice.text = "Rp 0"
            txtQuantity.text = "0"
        }

        buttonCheckOut.setOnClickListener {
            val item = ShoppingCart.getCartLength()
            if (item>0){
                val intent = Intent(applicationContext, PayTransactionActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(applicationContext,"Tidak ada produk yang di tambahkan",Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun getProduct(kategori:String,keyword:String,scan:Boolean){
        vm = ViewModelProvider(this,factory).get(ProdukViewModel::class.java).apply {
            viewState.observe(
                this@TransactionActivity,
                Observer(this@TransactionActivity::handleState)
            )
            if (viewState.value?.data == null) getProduks(kategori,keyword)
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
        if (data.size >0){
            adapter.updateData(data)
            layoutLoading.visibility = View.GONE
            layoutContent.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            layoutEmpty.visibility = View.GONE
        }else{
            layoutLoading.visibility = View.GONE
            layoutEmpty.visibility = View.VISIBLE
        }
    }

    private fun showError(error: Exception) {
        error.printStackTrace()
        recyclerView.visibility = View.GONE
    }

    private fun toggleLoading(loading: Boolean) {
        swiptToRefresh.isRefreshing = loading
        layoutContent.visibility = View.GONE
        layoutEmpty.visibility = View.GONE
        layoutLoading.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val scannedResult = result.contents
                searchText.setQuery(scannedResult, true)
                searchText.setIconified(false)
                searchText.requestFocusFromTouch()
                getProduct("all",scannedResult, true)
            } else {
                Toast.makeText(this@TransactionActivity, "Failed Scan", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
