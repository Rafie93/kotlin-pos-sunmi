package com.zerone.zerone_pos.ui.produk
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zerone.zerone_pos.repository.ProdukRepository

class ProdukViewModelFactory(
    private val produkRepository: ProdukRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProdukViewModel::class.java))
            return ProdukViewModel(produkRepository) as T
        throw IllegalArgumentException()
    }
}