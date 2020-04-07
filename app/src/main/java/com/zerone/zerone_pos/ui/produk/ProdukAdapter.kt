package com.zerone.zerone_pos.ui.produk

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zerone.zerone_pos.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.list_produk.*


class ProdukAdapter : RecyclerView.Adapter<ProdukAdapter.ViewHolder>() {
    private val produks = mutableListOf<ProdukModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_produk, parent, false)
    )

    override fun getItemCount() = produks.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(produks[position])
    }

    fun updateData(newProduk: MutableList<ProdukModel>) {
        produks.clear()
        produks.addAll(newProduk)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bindItem(produk: ProdukModel) {
            Glide.with(containerView!!)
                .load(produk.image)
                .into(produkImage)
            produkName.text = produk.name
            produkStok.text = produk.stock
            itemView.setOnClickListener {
                val intent = Intent(itemView.context.applicationContext, EditProdukActivity::class.java)
                intent.putExtra("id",produk.id)
                intent.putExtra("name",produk.name)
                intent.putExtra("code",produk.code)
                intent.putExtra("basic_price",produk.basic_price)
                intent.putExtra("selling_price",produk.selling_price)
                intent.putExtra("category",produk.category)!!
                intent.putExtra("is_stock",produk.is_stock)
                intent.putExtra("stock",produk.stock)
                intent.putExtra("description",produk.description)!!
                intent.putExtra("image",produk.image)
                intent.putExtra("satuan",produk.unit)
                itemView.context.startActivity(intent)
            }
        }
    }
}