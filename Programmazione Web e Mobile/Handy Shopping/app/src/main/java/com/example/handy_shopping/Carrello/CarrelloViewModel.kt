package com.example.handy_shopping.Carrello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.handy_shopping.retrofit.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarrelloViewModel : ViewModel() {
    private val _carrelloItems = MutableLiveData<List<CarrelloItem>>()
    val carrelloItems: LiveData<List<CarrelloItem>> get() = _carrelloItems

    fun fetchCartItems(userId: Int) {
        Client.retrofit.getCarrello(userId).enqueue(object : Callback<List<CarrelloItem>> {
            override fun onResponse(call: Call<List<CarrelloItem>>, response: Response<List<CarrelloItem>>) {
                if (response.isSuccessful) {
                    _carrelloItems.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<CarrelloItem>>, t: Throwable) {
            }
        })
    }
}
