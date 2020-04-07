package com.zerone.zerone_pos.ui.home
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.Formatt
import com.zerone.zerone_pos.ui.produk.ProdukActivity
import com.zerone.zerone_pos.ui.transaction.TransactionActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_chat_sales.*
import kotlinx.android.synthetic.main.item_menu_pembelian.*
import kotlinx.android.synthetic.main.item_menu_produk.*
import kotlinx.android.synthetic.main.item_menu_profit.*
import kotlinx.android.synthetic.main.item_menu_stock.*
import kotlinx.android.synthetic.main.item_menu_transaksi.*
import kotlinx.android.synthetic.main.overview_sales.*
import java.util.*

class HomeFragment : Fragment() {

    var entries = ArrayList<PieEntry>()
    private val viewModel: HomeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(viewModel) {
            sales_total.observe(this@HomeFragment, Observer {
                txtSalesTotal.text = getString(R.string.total_sales)+" \n"+"Rp. "+ Formatt.intToRibuan(it)
            })
            sales_day.observe(this@HomeFragment, Observer {
                txtSalesDay.text = Formatt.intToRibuan(it)
            })
            sales_month.observe(this@HomeFragment, Observer {
                txtSalesMonth.text = Formatt.intToRibuan(it)
            })
            home_response_data.observe(this@HomeFragment, Observer {
                bestSeller(it)
            })
        }
        klikTransaksi.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity!!.applicationContext, TransactionActivity::class.java)
            startActivity(intent)
        })
        klikProduk.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity!!.applicationContext, ProdukActivity::class.java)
            startActivity(intent)
        })
        klikPembelian.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity!!.applicationContext,getString(R.string.fitur_not_found),Toast.LENGTH_SHORT).show()
        })
        klikStock.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity!!.applicationContext,getString(R.string.fitur_not_found),Toast.LENGTH_SHORT).show()
        })
        klikProfit.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity!!.applicationContext,getString(R.string.fitur_not_found),Toast.LENGTH_SHORT).show()
        })

    }

    fun bestSeller(it: HomeModel?) {
        if (it!!.best_seller_month.isNotEmpty()) {
            val bestSellerMonth = it.best_seller_month
            val jumlahBestSeller = bestSellerMonth.size
            var totalTerjual =0
            var arrIndex = 0
            entries.clear()
            while (arrIndex < jumlahBestSeller){
                val nilai = bestSellerMonth[arrIndex].total
                val label = bestSellerMonth[arrIndex].label
                entries.add(PieEntry(nilai.toFloat(), label, arrIndex))
                totalTerjual+=nilai
                txtTotal.setText(totalTerjual.toString())
                arrIndex++
            }
            drawPieChart()

        }
    }

    private fun drawPieChart() {
        val dataSet = PieDataSet(entries, "")
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val data = PieData(dataSet)
        data.setValueTextSize(9f)
        data.setValueTextColor(Color.WHITE)
        val description =
            Description()
        description.text = ""
        pieChart.setDescription(description)
        pieChart.setDrawHoleEnabled(true)
        pieChart.setTransparentCircleRadius(58f)
        pieChart.setHoleRadius(58f)
        pieChart.animateXY(3000, 3000)
        pieChart.setData(data)
        pieChart.invalidate()
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(9f)
        pieChart.getLegend().setEnabled(false)

    }
}
