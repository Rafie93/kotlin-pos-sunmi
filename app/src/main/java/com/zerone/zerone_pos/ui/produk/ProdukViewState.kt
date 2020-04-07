package com.zerone.zerone_pos.ui.produk
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import java.lang.Exception

data class ProdukViewState (
    val loading: Boolean = false,
    val error: Exception? = null,
    val data: MutableList<ProdukModel>? = null,
    val postToServer : Boolean = false
)