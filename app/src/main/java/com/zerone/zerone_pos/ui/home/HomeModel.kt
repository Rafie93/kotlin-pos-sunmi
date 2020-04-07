package com.zerone.zerone_pos.ui.home

data class HomeModel (
    val sales_total:Int,
    val sales_day:Int,
    val sales_month:Int,
    val best_seller_month:ArrayList<BestSellerList>
)