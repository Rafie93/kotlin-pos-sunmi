package com.zerone.zerone_pos.ui.produk

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "produk")
data class ProdukModel (
    @PrimaryKey var id: String,
    var name: String?,
    var category: String?=null,
    var code: String?,
    var basic_price: String?,
    var selling_price: String?,
    var unit: String?,
    var description: String?=null,
    var is_ppn: Int?,
    var is_stock: Int?,
    var stock: String?,
    @SerializedName("image") var image: String?
): Parcelable {
    data class ProdukResponse(
        var data: MutableList<ProdukModel>
    )
}