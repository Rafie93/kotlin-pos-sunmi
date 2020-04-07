package com.zerone.zerone_pos.ui.transaction
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonParser
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.*
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_pay_transaction.*
import kotlinx.android.synthetic.main.popup_add_note.*
import kotlinx.android.synthetic.main.popup_additional.*
import kotlinx.android.synthetic.main.popup_bayar.*
import kotlinx.android.synthetic.main.popup_discount.*
import kotlinx.android.synthetic.main.popup_tax.*
import java.math.BigDecimal
import java.math.RoundingMode


class PayTransactionActivity : AppCompatActivity() {
    lateinit var adapter: PayTransactionAdapter
    var notes=""
    var tax = 0 // persen
    var discount = 0 // persen
    var totalProduk = 0
    var additional = 0
    var totalSales = 0.0
    var granTotal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_transaction)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Paper.init(this)


        adapter = PayTransactionAdapter(this, ShoppingCart.getCart())
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter

        tax = SharedPrefControl.getInstance(applicationContext).getInt(Const.TAX)
        discount = SharedPrefControl.getInstance(applicationContext).getInt(Const.DISCOUNT)
        additional = SharedPrefControl.getInstance(applicationContext).getInt(Const.ADDITIONAL)

        initView()

        buttonTax.setOnClickListener(View.OnClickListener {
            popupTax()
        })
        buttonDiscount.setOnClickListener(View.OnClickListener {
            popupDiscount()
        })
        buttonAdditional.setOnClickListener(View.OnClickListener {
            popupAdditional()
        })
        buttonAddnote.setOnClickListener(View.OnClickListener {
            popupAddNote()
        })

        editTextBayar.setOnClickListener(View.OnClickListener {
            popUpPembayaran()
        })

        //END PROSES
        buttonProses.setOnClickListener(View.OnClickListener {
            val bayar = editTextBayar.text.toString().trim()
            if(bayar.isEmpty()){
                editTextBayar.error = "required"
                editTextBayar.requestFocus()
                return@OnClickListener
            }
            if (bayar.toInt() < granTotal.toInt()){
                editTextBayar.error = "pembayaran masih kurang"
                editTextBayar.requestFocus()
                return@OnClickListener
            }
            ShoppingCart.getCart();
            var strBuilder = StringBuilder()
            strBuilder.append("[")
            var i = 0
            while (i < ShoppingCart.getCartLength()) {
                val id = ShoppingCart.getCart().get(i).product?.id;
                val quantity = ShoppingCart.getCart().get(i).quantity
                val priceSales = ShoppingCart.getCart().get(i).priceItem
                var priceProduk = ShoppingCart.getCart().get(i).product?.selling_price
                var pp = priceProduk
                if (priceProduk.toString().equals("null")){
                    pp==ShoppingCart.getCart().get(i).priceItem.toString()
                }

                var jsProd =
                    "{" +
                            "'id'"+":"+id+","+
                            "'quantity'"+":"+quantity+","+
                            "'priceSales'"+":"+priceSales+","+
                            "'priceProduk'"+":"+pp+
                            "}"
                strBuilder.append(jsProd)
                if (i!=ShoppingCart.getCartLength()-1){
                    strBuilder.append(",")
                }

                i++
            }
            strBuilder.append("]")
            val prodSold = strBuilder.toString()

            val jsonElement = JsonParser().parse(prodSold)
            val jsonArray = jsonElement.asJsonArray
            val json_product = jsonArray.toString()

            prosesTransaksi(bayar,json_product)
        })
    }

     fun popUpPembayaran() {
        val dialog = Dialog(this@PayTransactionActivity)
        dialog.setContentView(R.layout.popup_bayar)
        dialog.editJumlahHarusDibayar.setText("Rp."+Formatt.DoubleToRibuan(granTotal))
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

         dialog.buttonPas.setOnClickListener {
            val newBayar = granTotal.toInt().toString()
            editTextBayar.setText(newBayar)
            dialog.dismiss()
        }
         dialog.buttonLima.setOnClickListener {
             val newBayar = "5000"
             editTextBayar.setText(newBayar)
             dialog.dismiss()
         }
         dialog.buttonSepuluh.setOnClickListener {
             val newBayar = "10000"
             editTextBayar.setText(newBayar)
             dialog.dismiss()
         }
         dialog.buttonDuapulu.setOnClickListener {
             val newBayar = "20000"
             editTextBayar.setText(newBayar)
             dialog.dismiss()
         }
         dialog.buttonLimapuluh.setOnClickListener {
             val newBayar = "50000"
             editTextBayar.setText(newBayar)
             dialog.dismiss()
         }
         dialog.buttonSeratus.setOnClickListener {
             val newBayar = "100000"
             editTextBayar.setText(newBayar)
             dialog.dismiss()
         }
         dialog.buttonSet.setOnClickListener {
            val newBayar = dialog.editJumlahLain.text.toString()
            editTextBayar.setText(newBayar)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun initView(){

        txtSetTax.text = "Tax : "+tax.toString()+"%"
        txtSetDiscount.text = "Discount : "+discount.toString()+"%"
        txtSetAdditional.text = "Additional : Rp."+additional.toString()
        hitungTransaksi()
    }
    fun popupTax(){
        val dialog = Dialog(this@PayTransactionActivity)
        dialog.setContentView(R.layout.popup_tax)
        dialog.editTextTax.setText(tax.toString())
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.buttonSaveTax.setOnClickListener {
            val newTax = dialog.editTextTax.text.toString()
            tax = newTax.toInt()
            SharedPrefControl.getInstance(applicationContext).saveInt(Const.TAX,tax)
            dialog.dismiss()
            initView()
        }
        dialog.show()
    }
    fun popupAdditional(){
        val dialog = Dialog(this@PayTransactionActivity)
        dialog.setContentView(R.layout.popup_additional)
        dialog.editTextTax.setText(additional.toString())
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.buttonSaveTax.setOnClickListener {
            val newAdditional = dialog.editTextAdditional.text.toString()
            additional = newAdditional.toInt()
            SharedPrefControl.getInstance(applicationContext).saveInt(Const.ADDITIONAL,additional)
            dialog.dismiss()
            initView()
        }
        dialog.show()
    }
    fun popupDiscount(){
        val dialog = Dialog(this@PayTransactionActivity)
        dialog.setContentView(R.layout.popup_discount)
        dialog.editTextDiscount.setText(discount.toString())
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.buttonSaveDiscount.setOnClickListener {
            val newDiscount = dialog.editTextDiscount.text.toString()
            discount = newDiscount.toInt()
            SharedPrefControl.getInstance(applicationContext).saveInt(Const.DISCOUNT,discount)
            dialog.dismiss()
            initView()
        }
        dialog.show()
    }

    fun hitungTransaksi(){
        totalProduk = ShoppingCart.getCart().fold(0.toInt()) {
                acc, cartItem -> acc + cartItem.quantity.times(cartItem.priceItem!!.toInt())
        }
        val salesDiskon = (totalProduk * discount) / 100
         totalSales = totalProduk.toDouble() - salesDiskon.toDouble()
        val salesTax = ((totalSales + additional.toDouble()) * tax) / 100
        val totalGrant = totalSales + salesTax

        granTotal = (Math.ceil(totalGrant/100.0))*100
        txtTotal.text = "Rp.${Formatt.DoubleToRibuan(granTotal)}"
    }

    fun prosesTransaksi(pay:String,json_product:String){
        SharedPrefControl.getInstance(applicationContext).saveString(Const.CART_PRODUK,json_product)
        val total_product = totalProduk
        val total_sales = totalSales
        val grand_total = granTotal
        val change = grand_total.toInt()-pay.toInt()
        val invoice = TransactionHelper.createInvoice()
        val intent = Intent(applicationContext, ChoosePayActivity::class.java)
        intent.putExtra("invoice",invoice)
        intent.putExtra("total_product",total_product)
        intent.putExtra("total_sales",total_sales)
        intent.putExtra("tax",tax)
        intent.putExtra("discount",discount)
        intent.putExtra("additional",additional)
        intent.putExtra("grand_total",grand_total)
        intent.putExtra("pay",pay)
        intent.putExtra("change",Formatt.deleteMin(change.toString()))
        intent.putExtra("notes",notes)
        startActivity(intent)

    }

    fun popupAddNote(){
        val dialog = Dialog(this@PayTransactionActivity)
        dialog.setContentView(R.layout.popup_add_note)
        dialog.editTextNotePop.setText(notes)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.buttonSaveNote.setOnClickListener {
            notes = dialog.editTextNotePop.text.toString()
            dialog.dismiss()
        }
        dialog.show()
    }
}
