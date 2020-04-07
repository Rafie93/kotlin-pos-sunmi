package com.zerone.zerone_pos.ui.transaction

import com.zerone.zerone_pos.ui.produk.ProdukModel

data class CartItem(
    var product: ProdukModel?,
    var quantity: Int = 0,
    var price:Int=0,
    var priceItem: Int=0)