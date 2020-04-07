package com.zerone.zerone_pos.ui.transaction
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.helpers.*
import com.zerone.zerone_pos.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_success_transaction.*
import java.io.IOException
import java.io.OutputStream
import java.util.*

class SuccessTransactionActivity : AppCompatActivity() {

    private var outputStream: OutputStream? = null
    var isLogo = false
    var invoice=""
    var total=""
    var pay=""
    var change=""
    var notes=""
    var tax = 0
    var discount = 0
    var additional = 0
    var grand_total=0.0
    var total_product = 0

    private var gambar: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_transaction)

        var bundle :Bundle ?=intent.extras
         invoice = bundle!!.getString("invoice")!!
         total_product = bundle!!.getInt("total_product")
        var total_sales = bundle!!.getDouble("total_sales")
         tax = bundle!!.getInt("tax")
         discount = bundle!!.getInt("discount")
         additional = bundle!!.getInt("additional")
         grand_total = bundle!!.getDouble("grand_total")
         pay = bundle!!.getString("pay")!!
         change = bundle!!.getString("change")!!
         notes = bundle!!.getString("notes")!!

        txtOrderID.setText(invoice)
        txtGrant.setText(Formatt.DoubleToRibuan(grand_total))
        txtBayar.setText(Formatt.intToRibuan(pay!!.toInt()))
        txtKembali.setText(Formatt.intToRibuan(change!!.toInt()))

        buttonKeluar.setOnClickListener {
            ShoppingCart.removeAll(applicationContext)
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        buttonNewTransaction.setOnClickListener {
            ShoppingCart.removeAll(applicationContext)
            val intent = Intent(applicationContext, TransactionActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonPrint.setOnClickListener(View.OnClickListener {
            cetakStruk()
        })
    }

    //cetak struk
    fun cetakStruk() {
        var socket: BluetoothSocket? = null
        //Get BluetoothAdapter
        val btAdapter = PrinterBluetooth.getBTAdapter()
        if (btAdapter == null) {
            Toast.makeText(baseContext, "Open Bluetooth", Toast.LENGTH_SHORT).show()
            return
        }
        // Get sunmi InnerPrinter BluetoothDevice
        val device = PrinterBluetooth.getDevice(btAdapter)
        if (device == null) {
            Toast.makeText(baseContext, "Pastikan Bluetooth tersambung ke InnterPrinter", Toast.LENGTH_LONG)
                .show()
            return
        }

        try {
            socket = PrinterBluetooth.getSocket(device)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            socket?.let {
                outputStream = socket.outputStream
                val printformat = byteArrayOf(0x1B, 0x21, 0x03)
                val you =  SharedPrefControl.getInstance(applicationContext).getString(Const.USER_NAMA)
                val merchant_name =  SharedPrefControl.getInstance(applicationContext).getString(Const.MERCHANT_NAME)
                val merchant_address =  SharedPrefControl.getInstance(applicationContext).getString(Const.MERCHANT_ADDRESS)
                val merchant_city =  SharedPrefControl.getInstance(applicationContext).getString(Const.MERCHANT_CITY)

                outputStream?.write(printformat)
                if (isLogo) printPhotoBitmap(gambar!!)
                printCustom(merchant_name!!, 1, 1)
                printCustom(merchant_address!! +" "+ merchant_city!!, 0, 1)
                printCustom(getString(R.string.cashier)+":"+you!!, 1, 0)
                printCustom("--------------------------------", 0, 1)
                printCustom(invoice, 2, 1)
                val dateTime = TransactionHelper.getDateTime()
                printCustom(dateTime[0]+" "+dateTime[1], 0, 0)
                printCustom("--------------------------------", 0, 1)
                var i = 0
                while (i < ShoppingCart.getCartLength()) {
                    val nameProduk =ShoppingCart.getCart().get(i).product?.name
                    val quantity = ShoppingCart.getCart().get(i).quantity
                    var subTotal =ShoppingCart.getCart().get(i).price
                    var disSubTotal = Formatt.intToRibuan(subTotal)
                    printText(leftRightAlign(quantity.toString()+" "+nameProduk.toString().toLowerCase()+
                            " ",disSubTotal.toString() ),4)
                    i++
                }
                printCustom("--------------------------------", 0, 1)
                printText(leftRightAlign("Sub Total",Formatt.intToRibuan(total_product).toString()),0)
                if (additional>0){
                    printText(leftRightAlign("Additional",Formatt.intToRibuan(additional).toString()),0)
                }
                if (discount>0) {
                    printText(leftRightAlign("Discount", discount.toString() + "%"), 0)
                }
                printText(leftRightAlign("Tax",tax.toString()+"%"),0)
                printText(leftRightAlign("Grand Total",Formatt.DoubleToRibuan(grand_total).toString()),3)
                printText(leftRightAlign("Pay",Formatt.intToRibuan(pay.toInt()).toString()),0)
                printText(leftRightAlign("Change",Formatt.intToRibuan(change.toInt()).toString()),0)

                if (!notes.equals("")) {
                    printNewLine()
                    printCustom("Notes :" + notes, 0, 0)
                }
                printCustom("--------------------------------", 0, 1)
                printCustom("*Terima kasih atas kunjungannya*",0,1)
                printPhoto(R.drawable.zeroone)
                printNewLine()
                printNewLine()
                printNewLine()

                outputStream?.flush()

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    //SETTING PAPER STRUK
    fun printCustom(msg: String, size: Int, align: Int) {
        val cc = byteArrayOf(0x1B, 0x21, 0x03)  // 0- normal size text
        val bb = byteArrayOf(0x1B, 0x21, 0x08)  // 1- only bold text
        val bb2 = byteArrayOf(0x1B, 0x21, 0x20) // 2- bold with medium text
        val bb3 = byteArrayOf(0x1B, 0x21, 0x10) // 3- bold with large text
        try {
            when (size) {
                0 -> outputStream?.write(cc)
                1 -> outputStream?.write(bb)
                2 -> outputStream?.write(bb2)
                3 -> outputStream?.write(bb3)
            }

            when (align) {
                0 -> outputStream?.write(PrinterCommands.ESC_ALIGN_LEFT)
                1 -> outputStream?.write(PrinterCommands.ESC_ALIGN_CENTER)
                2 -> outputStream?.write(PrinterCommands.ESC_ALIGN_RIGHT)
            }
            outputStream?.write(msg.toByteArray())
            outputStream?.write(0x0A)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun printNewLine() {
        try {
            outputStream?.write(PrinterCommands.FEED_LINE)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    fun printText(msg: String,size: Int) {
        val cc = byteArrayOf(0x1B, 0x21, 0x03)  // 0- normal size text
        val bb = byteArrayOf(0x1B, 0x21, 0x08)  // 1- only bold text
        val bb2 = byteArrayOf(0x1B, 0x21, 0x20) // 2- bold with medium text
        val bb3 = byteArrayOf(0x1B, 0x21, 0x10) // 3- bold with large text
        val bb4 = byteArrayOf(0x1B, 0x21, 0x01)
        try {
            when (size) {
                0 -> outputStream?.write(cc)
                1 -> outputStream?.write(bb)
                2 -> outputStream?.write(bb2)
                3 -> outputStream?.write(bb3)
                4 -> outputStream?.write(bb4)
            }
            outputStream?.write(msg.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    //print byte[]
    private fun printText(msg: ByteArray) {
        try {
            // Print normal text
            outputStream?.write(msg)
            printNewLine()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    //print photo
    fun printPhoto(img: Int) {
        try {
            val bmp = BitmapFactory.decodeResource(
                resources,
                img
            )
            if (bmp != null) {
                val command = PrinterUtils.decodeBitmap(bmp)
                outputStream?.write(PrinterCommands.ESC_ALIGN_CENTER)
                printText(command)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun printPhotoBitmap(bmp: Bitmap) {
        try {

            if (bmp != null) {
                val command = PrinterUtils.decodeBitmap(bmp)
                outputStream?.write(PrinterCommands.ESC_ALIGN_CENTER)
                printText(command)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    fun leftRightAlign(str1: String, str2: String): String {
        var ans = str1 + str2
        if (ans.length <= 25) {
            val tot = str1.length + str2.length
            val n = (22 - str1.length) + (7 - str2.length)
            ans = str1 + String(CharArray(n)).replace("\u0000", " ") + str2
        }else{
            val n = (7 - str2.length)
            ans = str1.substring(0,23) + ".." + String(CharArray(n)).replace("\u0000", " ")   + str2
        }
        ans+="\n"
        return ans
    }
}
