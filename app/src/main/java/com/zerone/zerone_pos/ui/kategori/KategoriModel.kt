package com.zerone.zerone_pos.ui.kategori

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "kategori")
data class KategoriModel (
    @PrimaryKey var id: String,
    var category: String?=null
): Parcelable {
    data class KategoriResponse(
        var data: MutableList<KategoriModel>
    )
}