// HomeViewModel.kt
package com.example.handy_shopping.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.handy_shopping.retrofit.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _discountedProducts = MutableLiveData<List<ProdottoItem>>()
    val discountedProducts: LiveData<List<ProdottoItem>> get() = _discountedProducts



    fun fetchDiscountedProducts() {
        Client.retrofit.getDiscountedProducts().enqueue(object : Callback<List<ProdottoItem>> {
            override fun onResponse(call: Call<List<ProdottoItem>>, response: Response<List<ProdottoItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _discountedProducts.postValue(it)
                    } ?: run {
                        _discountedProducts.postValue(emptyList())
                    }
                } else {
                    _discountedProducts.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<ProdottoItem>>, t: Throwable) {
                _discountedProducts.postValue(emptyList())
            }
        })
    }
}