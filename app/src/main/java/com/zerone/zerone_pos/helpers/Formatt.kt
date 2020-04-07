package com.zerone.zerone_pos.helpers

/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */

import java.text.DecimalFormat

class Formatt {
    companion object {
        fun intToRibuan(angka: Int): String? {
            val formatter = DecimalFormat("#,###")
            val myNumber = angka
            val formattedNumber = formatter.format(myNumber)
            return formattedNumber
        }
        fun DoubleToRibuan(angka: Double): String? {
            val formatter = DecimalFormat("#,###")
            val myNumber = angka
            val formattedNumber = formatter.format(myNumber)
            return formattedNumber
        }

        fun ribuatToInt(display:String): Int {
            val res = display.replace(",","")
            return res.toInt()
        }
        fun ribuatToDouble(display:String): Double {
            val res = display.replace(",","")
            return res.toDouble()
        }
        fun deleteMin(display: String):String{
            val res = display.replace("-","")
            return res
        }
    }

}