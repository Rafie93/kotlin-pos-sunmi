package com.zerone.zerone_pos.ui.transaction
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.Formatt
import com.zerone.zerone_pos.helpers.SharedPrefControl
import com.zerone.zerone_pos.helpers.ShoppingCart
import com.zerone.zerone_pos.ui.produk.ProdukModel
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.android.synthetic.main.list_produk.*
import kotlinx.android.synthetic.main.popup_add_produk.*
import kotlinx.android.synthetic.main.popup_add_produk.txtPrice

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    private val produks = mutableListOf<ProdukModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_produk, parent, false)
    )

    override fun getItemCount() = produks.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(produks[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bindItem(produk: ProdukModel) {
            val sellingPrice = produk.selling_price
            Glide.with(containerView!!)
                .load(produk.image)
                .into(produkImage)
            produkName.text = produk.name
            produkStok.text = produk.stock
            produkPriceSelling.text = "Rp "+ Formatt.intToRibuan(sellingPrice!!.toInt())

            itemView.setOnClickListener(View.OnClickListener {
                val dialog = Dialog(itemView.context)
                val hargaModal = produk!!.basic_price!!.toInt()
                val hargaJual = produk!!.selling_price!!.toInt()
                dialog.setContentView(R.layout.popup_add_produk)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.txtProduk.setText(produk!!.name)
                dialog.txtPrice.setText("Rp."+Formatt.intToRibuan(hargaModal!!))
                dialog.editTextHarga.setText(hargaJual.toString())
                if (!produk!!.image.isNullOrEmpty()){
                    Glide.with(itemView.context)
                        .load(produk!!.image)
                        .into(dialog.imgReview)
                }

                Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {
                    dialog.buttonOK.setOnClickListener { view ->
                        var q : Int    = 0
                        val eJ : String = dialog.editTextJumlah.text.toString()
                        var harga : Int    = 0
                        val tH : String = dialog.editTextHarga.text.toString()
                        q = eJ.toInt()
                        harga = tH.toInt()

                        if (produk.is_stock ==1){
                            if (produk.stock!!.toInt() < q){
                                Toast.makeText(itemView.context,"Stock Tidak Mencukupi",Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }
                        }


                        val item = CartItem(produk,q, harga*q,harga)
                        ShoppingCart.addItem(item)
                        it.onNext(ShoppingCart.getCart())

                    }

                }).subscribe { cart ->

                    var quantity = ShoppingCart.getShoppingCartSize()
                    var total = ShoppingCart.getCart()
                        .fold(0.toInt()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.priceItem!!.toInt()) }
                    (itemView.context as TransactionActivity).txtQuantity.text = quantity.toString()
                    (itemView.context as TransactionActivity).txtPrice.text = "Rp "+Formatt.intToRibuan(total.toInt())
                    (itemView.context as TransactionActivity).layoutCheckout.visibility = View.VISIBLE
                    dialog.dismiss()
                }

                dialog.show()
            })
        }

    }

    fun updateData(newProduk: MutableList<ProdukModel>) {
        produks.clear()
        produks.addAll(newProduk)
        notifyDataSetChanged()
    }
}