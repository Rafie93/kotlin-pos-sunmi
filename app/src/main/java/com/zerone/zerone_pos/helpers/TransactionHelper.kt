package com.zerone.zerone_pos.helpers
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Environment
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class TransactionHelper {
    companion object {
         fun createInvoice(): String {
            val c = Calendar.getInstance()
            var invoice =
                "INV" + c.get(Calendar.YEAR).toString() + (c.get(Calendar.MONTH) + 1).toString() + c.get(
                    Calendar.DAY_OF_MONTH
                ).toString() +
                        c.get(Calendar.HOUR_OF_DAY).toString() + c.get(Calendar.MINUTE) + c.get(
                    Calendar.SECOND
                );

            return invoice
        }

        fun getDateTime(): Array<String?> {
            val c = Calendar.getInstance()
            val dateTime = arrayOfNulls<String>(2)
            dateTime[0] = c.get(Calendar.YEAR).toString() + "-" + (c.get(Calendar.MONTH)+1) + "-" + c.get(
                Calendar.DAY_OF_MONTH)
            dateTime[1] = c.get(Calendar.HOUR_OF_DAY).toString() + ":" + c.get(Calendar.MINUTE)+":"+ c.get(
                Calendar.SECOND)
            return dateTime
        }

        fun  hasNetworkAvailable(context: Context): Boolean {
            val service = Context.CONNECTIVITY_SERVICE
            val manager = context.getSystemService(service) as ConnectivityManager?
            val network = manager?.activeNetworkInfo
            return (network != null)
        }

        fun createPartFromString(descriptionString: String): RequestBody {
            return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString
            )
        }

        fun createTempFile(context: Context, bitmap: Bitmap): File {
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                System.currentTimeMillis().toString() + ".JPEG"
            )
            val bos = ByteArrayOutputStream()

            bitmap.compress(Bitmap.CompressFormat.JPEG, 32, bos)
            val bitmapdata = bos.toByteArray()

            try {
                val fos = FileOutputStream(file)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return file
        }
    }
}