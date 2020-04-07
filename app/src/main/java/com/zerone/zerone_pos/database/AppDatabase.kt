package com.zerone.zerone_pos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zerone.zerone_pos.ui.kategori.KategoriModel
import com.zerone.zerone_pos.ui.produk.ProdukModel
import com.zerone.zerone_pos.ui.transaction.TransactionModel

@Database(entities = arrayOf(
    KategoriModel::class,
    ProdukModel::class,
    TransactionModel::class),
    version = 3)

abstract class AppDatabase : RoomDatabase() {
    abstract fun produkDao(): ProdukDao
    abstract fun kategoriDao(): KategoriDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            instance?.let { return it }
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "zerone-pos-v3"
            ).build()
            return instance!!
        }
    }
}