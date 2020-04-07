package com.zerone.zerone_pos.ui.transaction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.Const
import com.zerone.zerone_pos.helpers.Formatt
import com.zerone.zerone_pos.helpers.SharedPrefControl
import com.zerone.zerone_pos.helpers.ShoppingCart
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.activity_pay_transaction.*
import kotlinx.android.synthetic.main.list_cart.view.*


class PayTransactionAdapter(var context: Context, var cartItems: List<CartItem>) :
    RecyclerView.Adapter<PayTransactionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PayTransactionAdapter.ViewHolder {

        val layout = LayoutInflater.from(context).inflate(R.layout.list_cart, parent, false)

        return ViewHolder(layout)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(viewHolder: PayTransactionAdapter.ViewHolder, position: Int) {
        viewHolder.bindItem(cartItems[position])
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(cartItem: CartItem) {
            val nameProduk =cartItem.product?.name
            itemView.txtProdukName.text = nameProduk
            itemView.txtHarga.text = "Rp. ${Formatt.intToRibuan(cartItem.priceItem)}"
            itemView.edtQuantityItem.setText(cartItem.quantity.toString())
            var qty = cartItem.quantity

            Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {

                itemView.btnAddLagi.setOnClickListener { view ->
                    ShoppingCart.addItem2(cartItem)
                    qty += 1
                    itemView.edtQuantityItem.setText(qty.toString())
                    it.onNext(ShoppingCart.getCart())

                }

                itemView.btnRemoveItem.setOnClickListener {view ->
                    val edtQTY = itemView.edtQuantityItem.text.toString().trim()
                    if (!edtQTY.equals("0")){
                        ShoppingCart.removeItem(cartItem, itemView.context)
                        qty -= 1
                        itemView.edtQuantityItem.setText(qty.toString())
                        it.onNext(ShoppingCart.getCart())
                    }
                }


            }).subscribe { cart ->

                var totalProduk = ShoppingCart.getCart()
                    .fold(0.toInt()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.priceItem!!.toInt()) }

                val tax = SharedPrefControl.getInstance(itemView.context.applicationContext).getInt(Const.TAX)
                val discount = SharedPrefControl.getInstance(itemView.context.applicationContext).getInt(Const.DISCOUNT)
                val additional = SharedPrefControl.getInstance(itemView.context.applicationContext).getInt(Const.ADDITIONAL)

                val salesDiskon = (totalProduk * discount) / 100
                val totalSales = totalProduk.toDouble() - salesDiskon.toDouble()
                val salesTax = ((totalSales + additional) * tax) / 100
                val totalGrant = totalSales + salesTax

                val granTotal = (Math.ceil(totalGrant/100.0))*100

                val showAkhir = Formatt.DoubleToRibuan(granTotal)
                (itemView.context as PayTransactionActivity).txtTotal.text = "Rp "+showAkhir
            }

        }


    }

}