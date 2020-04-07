package com.zerone.zerone_pos.ui.produk
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */
import androidx.lifecycle.*
import com.zerone.zerone_pos.repository.ProdukRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap

class ProdukViewModel(
    private val produkRepository: ProdukRepository
    ) : ViewModel() {
    private val mViewState = MutableLiveData<ProdukViewState>().apply {
        value = ProdukViewState(loading = true)
    }
    val viewState: LiveData<ProdukViewState>
        get() = mViewState

    fun getProduks(kategori: String,keyword:String) = viewModelScope.launch {
        try {
            val data = produkRepository.getProduks(kategori,keyword)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data)
        } catch (ex: Exception) {
            mViewState.value = mViewState.value?.copy(loading = false, error = ex, data = null)
        }
    }

    fun storeProduk(
        code:String,name:String,basicPrice:String,sellingPrice:String,isStok:Int,unit:String,category:String,stock:String,deskripsi:String
    ) = viewModelScope.launch {
        try {
            val data = produkRepository.addSingle(code,name,basicPrice,sellingPrice,isStok,unit,category,stock,deskripsi)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data,postToServer = true)
        } catch (ex: Exception) {
            mViewState.value = mViewState.value?.copy(loading = false, error = ex, data = null,postToServer = false)
        }
    }
    fun storeProdukWithImage(
        body: MultipartBody.Part,
        map: HashMap<String, RequestBody>
    ) = viewModelScope.launch {
        try {
            val data = produkRepository.addSingleWithImage(body,map)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data,postToServer = true)
        } catch (ex: Exception) {
            mViewState.value = mViewState.value?.copy(loading = false, error = ex, data = null,postToServer = false)
        }
    }

    fun updateProduk(
        code:String,name:String,basicPrice:String,sellingPrice:String,isStok:Int,unit:String,category:String,stock:String,deskripsi:String,id: String
    ) = viewModelScope.launch {
        try {
            val data = produkRepository.updateProduk(code,name,basicPrice,sellingPrice,isStok,unit,category,stock,deskripsi,id)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data,postToServer = true)
        } catch (ex: Exception) {
            mViewState.value = mViewState.value?.copy(loading = false, error = ex, data = null,postToServer = false)
        }
    }

    fun updateProdukWithImage(
        id: String,
        body: MultipartBody.Part,
        map: HashMap<String, RequestBody>
    ) = viewModelScope.launch {
        try {
            val data = produkRepository.updateWithImage(id,body,map)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data,postToServer = true)
        } catch (ex: Exception) {
            mViewState.value = mViewState.value?.copy(loading = false, error = ex, data = null,postToServer = false)
        }
    }

    fun deleteProduk(id:String)= viewModelScope.launch {
        try {
            val data = produkRepository.deleteProduk(id)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data,postToServer = true)
        } catch (ex: Exception) {
            mViewState.value = mViewState.value?.copy(loading = false, error = ex, data = null,postToServer = false)
        }
    }

}