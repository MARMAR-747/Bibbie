package com.example.handy_shopping.DettagliProdotto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.handy_shopping.retrofit.Client
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class ReviewViewModel : ViewModel() {
    private val _reviews = MutableLiveData<MutableList<ReviewItem>>()
    val reviews: LiveData<MutableList<ReviewItem>> get() = _reviews

    private val _hasReviewed = MutableLiveData<Boolean>()
    val hasReviewed: LiveData<Boolean> get() = _hasReviewed

    private val _hasPurchased = MutableLiveData<Boolean>()
    val hasPurchased: LiveData<Boolean> get() = _hasPurchased

    fun fetchReviews(prodottoId: Int) {
        Client.retrofit.getProductReviews(prodottoId).enqueue(object : Callback<List<ReviewItem>> {
            override fun onResponse(call: Call<List<ReviewItem>>, response: Response<List<ReviewItem>>) {
                if (response.isSuccessful) {
                    _reviews.value = response.body()?.toMutableList() ?: mutableListOf()
                }
            }

            override fun onFailure(call: Call<List<ReviewItem>>, t: Throwable) {
            }
        })
    }

    fun updateReview(review: ReviewItem) {
        val currentTimestamp = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(System.currentTimeMillis())

        val reviewJson = JsonObject().apply {
            addProperty("idrecensione", review.idrecensione)
            addProperty("idutente", review.idutente)
            addProperty("testo", review.testo)
            addProperty("valutazione", review.valutazione)
            addProperty("idprodotto", review.idprodotto)
            addProperty("data_recensione", currentTimestamp)
        }

        Client.retrofit.updateReview(reviewJson).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val updatedReview = review.copy(data_recensione = currentTimestamp)
                    val currentReviews = _reviews.value ?: mutableListOf()
                    val updatedList = currentReviews.map {
                        if (it.idrecensione == updatedReview.idrecensione) updatedReview else it
                    }.toMutableList()
                    _reviews.value = updatedList
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            }
        })
    }

    fun deleteReview(review: ReviewItem, userId: Int) {
        val reviewJson = JsonObject().apply {
            addProperty("idrecensione", review.idrecensione)
            addProperty("idutente", userId)
        }

        Client.retrofit.deleteReview(reviewJson).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val currentReviews = _reviews.value ?: mutableListOf()
                    val updatedList = currentReviews.filterNot { it.idrecensione == review.idrecensione }.toMutableList()
                    _reviews.value = updatedList
                    _hasReviewed.value = false
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            }
        })
    }

    fun checkUserReview(prodottoId: Int, userId: Int) {
        val reviewCheck = JsonObject().apply {
            addProperty("prodotto_id", prodottoId)
            addProperty("user_id", userId)
        }

        Client.retrofit.hasReviewedProduct(reviewCheck).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    _hasReviewed.value = response.body()?.get("has_reviewed")?.asBoolean ?: false
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            }
        })
    }

    fun setHasPurchased(hasPurchased: Boolean) {
        _hasPurchased.value = hasPurchased
    }

}