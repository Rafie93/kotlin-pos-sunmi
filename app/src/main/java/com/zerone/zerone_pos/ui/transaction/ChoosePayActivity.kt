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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.Const
import com.zerone.zerone_pos.helpers.Formatt
import com.zerone.zerone_pos.helpers.SharedPrefControl
import com.zerone.zerone_pos.helpers.TransactionHelper
import com.zerone.zerone_pos.repository.TransactionRepository
import kotlinx.android.synthetic.main.activity_choose_pay.*

class ChoosePayActivity : AppCompatActivity() {
    private lateinit var vm: TransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_pay)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var bundle :Bundle ?=intent.extras
        var invoice = bundle!!.getString("invoice")
        var total_product = bundle!!.getInt("total_product")
        var total_sales = bundle!!.getDouble("total_sales")
        var tax = bundle!!.getInt("tax")
        var discount = bundle!!.getInt("discount")
        var additional = bundle!!.getInt("additional")
        var grand_total = bundle!!.getDouble("grand_total")
        var pay = bundle!!.getString("pay")
        var change = bundle!!.getString("change")
        var notes = bundle!!.getString("notes")

        txtOrderID.setText(invoice)
        txtTotal.setText(Formatt.intToRibuan(total_product))
        txtTax.setText(tax.toString()+"%")
        txtDiscount.setText(discount.toString()+"%")
        txtAdditional.setText(Formatt.intToRibuan(additional))
        txtGrant.setText(Formatt.DoubleToRibuan(grand_total))
        txtBayar.setText(Formatt.intToRibuan(pay!!.toInt()))
        txtKembali.setText(Formatt.intToRibuan(change!!.toInt()))

        payCash.setOnClickListener(View.OnClickListener {
            val dateTime = TransactionHelper.getDateTime()
            val date = dateTime[0]
            val time = dateTime[1]
            val product_transaction = SharedPrefControl.getInstance(applicationContext).getString(Const.CART_PRODUK)

            val factory = TransactionViewModelFactory(TransactionRepository.instance)
            vm = ViewModelProvider(this,factory).get(TransactionViewModel::class.java).apply {
                viewState.observe(
                    this@ChoosePayActivity,
                    Observer(this@ChoosePayActivity::handleState)
                )
                addTransactions(invoice.toString(),date.toString(),time.toString(),total_product.toString(),total_sales.toString(),additional.toString(),
                    discount.toString(),tax.toString(),grand_total.toString(),pay.toString(),change.toString(),
                    notes.toString(),product_transaction.toString())

            }
            val intent = Intent(applicationContext, SuccessTransactionActivity::class.java)
            intent.putExtra("invoice",invoice)
            intent.putExtra("total_product",total_product)
            intent.putExtra("total_sales",total_sales)
            intent.putExtra("tax",tax)
            intent.putExtra("discount",discount)
            intent.putExtra("additional",additional)
            intent.putExtra("grand_total",grand_total)
            intent.putExtra("pay",pay)
            intent.putExtra("change",Formatt.deleteMin(change.toString()))
            intent.putExtra("product_transaction",product_transaction)
            intent.putExtra("notes",notes)
            startActivity(intent)
            finish()
        })

    }
    private fun handleState(viewState: TransactionViewState?) {
        viewState?.let {
            it.error?.let { error -> showError(error) }
        }
    }

    private fun showError(error: Exception) {
        error.printStackTrace()
    }

}
