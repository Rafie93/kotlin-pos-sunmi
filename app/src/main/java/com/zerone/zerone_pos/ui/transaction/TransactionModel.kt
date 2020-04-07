package com.zerone.zerone_pos.ui.transaction

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "sale")
data class TransactionModel (
    @PrimaryKey var id: String,
    var invoice: String?,
    var date: String?,
    var time: String?,
    var total_product: Int,
    var total_sales: Double,
    var tax:Int,
    var additional:Int,
    var discount:Int,
    var grand_total:Double,
    var pay:Int,
    var change:Int,
    var notes:String?=null,
    var product_transaction:String?=null


): Parcelable {
    data class TransactionResponse(
        var data: MutableList<TransactionModel>
    )
}
