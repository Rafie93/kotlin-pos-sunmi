package com.zerone.zerone_pos

import android.app.Application
import com.zerone.zerone_pos.database.AppDatabase
import com.zerone.zerone_pos.datastore.kategori.KategoriRemoteDataStore
import com.zerone.zerone_pos.datastore.kategori.KategoriRoomDataStore
import com.zerone.zerone_pos.datastore.produk.ProdukRemoteDataStore
import com.zerone.zerone_pos.datastore.produk.ProdukRoomDataStore
import com.zerone.zerone_pos.datastore.transaction.TransactionRemoteDataStore
import com.zerone.zerone_pos.datastore.transaction.TransactionRoomDataStore
import com.zerone.zerone_pos.helpers.SharedPrefControl
import com.zerone.zerone_pos.repository.KategoriRepository
import com.zerone.zerone_pos.repository.ProdukRepository
import com.zerone.zerone_pos.repository.TransactionRepository
import com.zerone.zerone_pos.webservice.NetworkApp

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val auth = SharedPrefControl.getInstance(applicationContext).getToken()
        val zeronePosService = NetworkApp.ZERONE_POS_SERVICE
        val appDatabase = AppDatabase.getInstance(this)
        ProdukRepository.instance.apply {
            init(
                ProdukRoomDataStore(appDatabase.produkDao()),
                ProdukRemoteDataStore(zeronePosService,auth!!)
            )
        }
        KategoriRepository.instance.apply {
            init(
                KategoriRoomDataStore(appDatabase.kategoriDao()),
                KategoriRemoteDataStore(zeronePosService,auth!!)
            )
        }
        TransactionRepository.instance.apply {
            init(
                TransactionRoomDataStore(appDatabase.transactionDao()),
                TransactionRemoteDataStore(zeronePosService,auth!!)
            )
        }

    }
}