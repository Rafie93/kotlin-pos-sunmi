package com.zerone.zerone_pos.ui.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.Formatt
import com.zerone.zerone_pos.ui.transaction.TransactionModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_history.*

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private val historys = mutableListOf<TransactionModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_history, parent, false)
    )

    override fun getItemCount() = historys.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(historys[position])
    }

    fun updateData(newProduk: MutableList<TransactionModel>) {
        historys.clear()
        historys.addAll(newProduk)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bindItem(history: TransactionModel) {

            txtInvoice.text = history.invoice
            totalSales.text = Formatt.DoubleToRibuan(history.grand_total)
            txtDateTime.text = history.date + " "+history.time
            itemView.setOnClickListener {
                val intent = Intent(itemView.context.applicationContext, HistoryDetailActivity::class.java)
                intent.putExtra("invoice",history.invoice)
                intent.putExtra("total_product",history.total_product)
                intent.putExtra("total_sales",history.total_sales)
                intent.putExtra("tax",history.tax)
                intent.putExtra("discount",history.discount)
                intent.putExtra("additional",history.additional)
                intent.putExtra("grand_total",history.grand_total)
                intent.putExtra("pay",history.pay)
                intent.putExtra("change",history.change)
                intent.putExtra("notes",history.notes)
                itemView.context.startActivity(intent)
            }
        }
    }
}