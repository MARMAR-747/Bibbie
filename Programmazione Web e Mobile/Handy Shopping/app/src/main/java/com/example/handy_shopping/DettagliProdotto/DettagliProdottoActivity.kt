package com.example.handy_shopping.DettagliProdotto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.handy_shopping.databinding.ActivityDettagliProdottoBinding
import com.example.handy_shopping.retrofit.Client
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.activity.viewModels
import com.example.handy_shopping.Altro.NavigationActivity
import com.example.handy_shopping.Home.ProdottoItem

class DettagliProdottoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDettagliProdottoBinding
    private var prodottoId: Int = 0
    private var userId: Int = 0
    private var isUserPremium: Boolean = false
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private val refreshInterval = 10000L // 10 secondi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDettagliProdottoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        userId = sharedPreferences.getInt("userId", 0)
        isUserPremium = sharedPreferences.getBoolean("isPremium", false)

        prodottoId = intent.getIntExtra("prodotto_id", 0)
        if (prodottoId != 0) {
            fetchProductDetails(prodottoId)
            reviewViewModel.fetchReviews(prodottoId)
            reviewViewModel.checkUserReview(prodottoId, userId)
            checkUserPurchase(prodottoId, userId)
        } else {
            Toast.makeText(this, "Errore: ID prodotto non trovato", Toast.LENGTH_SHORT).show()
            finish()
        }

        //setOnClickListeners
        binding.back.setOnClickListener {
            finish()
        }

        binding.aggiungiCarrelloButton.setOnClickListener {
            addToCart()
        }

        binding.inviaRecensione.setOnClickListener {
            val reviewText = binding.boxRecensione.text.toString()
            val rating = binding.recensioneRating.rating
            if (reviewText.isNotBlank() && rating > 0) {
                submitReview(prodottoId, reviewText, rating)
            } else {
                Toast.makeText(
                    this,
                    "Inserisci una recensione e una valutazione",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        val reviewAdapter = ReviewAdapter(mutableListOf(), userId, reviewViewModel)
        binding.recensioniRecycler.apply {
            layoutManager = LinearLayoutManager(this@DettagliProdottoActivity)
            adapter = reviewAdapter
        }

        reviewViewModel.reviews.observe(this) { reviews ->
            reviewAdapter.updateReviews(reviews)
        }

        reviewViewModel.hasReviewed.observe(this) {
            updateReviewBoxVisibility()
        }

        reviewViewModel.hasPurchased.observe(this) {
            updateReviewBoxVisibility()
        }
        startAutoRefresh()
    }

    //funzioni
    private fun updateReviewBoxVisibility() {
        val hasPurchased = reviewViewModel.hasPurchased.value ?: false
        val hasReviewed = reviewViewModel.hasReviewed.value ?: false
        if (hasPurchased) {
            if (hasReviewed) {
                hideReviewBox()
            } else {
                showReviewBox()
            }
        } else {
            hideReviewBox()
        }
    }

    private fun startAutoRefresh() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                fetchProductDetails(prodottoId)
                handler.postDelayed(this, refreshInterval)
            }
        }, refreshInterval)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun hideReviewBox() {
        binding.inviaRecensione.visibility = View.GONE
        binding.boxRecensione.visibility = View.GONE
        binding.recensioneText.visibility = View.GONE
        binding.recensioneRating.visibility = View.GONE
    }

    private fun showReviewBox() {
        binding.inviaRecensione.visibility = View.VISIBLE
        binding.boxRecensione.visibility = View.VISIBLE
        binding.recensioneText.visibility = View.VISIBLE
        binding.recensioneRating.visibility = View.VISIBLE
    }

    private fun formatPrice(price: Double): String {
        return if (price == price.toInt().toDouble()) {
            "€${price.toInt()}"
        } else {
            String.format("€%.2f", price)
        }
    }

    //retrofit
    private fun fetchProductDetails(prodottoId: Int) {
        Client.retrofit.getProductDetails(prodottoId).enqueue(object : Callback<ProdottoItem> {
            override fun onResponse(call: Call<ProdottoItem>, response: Response<ProdottoItem>) {
                if (response.isSuccessful) {
                    response.body()?.let { prodotto ->
                        binding.marca.text = prodotto.marca
                        binding.nome.text = prodotto.nome
                        binding.voto.rating = prodotto.valutazione
                        binding.prezzo.text = formatPrice(if (prodotto.prezzo_scontato > 0) prodotto.prezzo_scontato else prodotto.prezzo)
                        binding.premium.visibility = if (prodotto.isPremium()) View.VISIBLE else View.GONE
                        binding.descrizione.text = prodotto.descrizione

                        Glide.with(this@DettagliProdottoActivity)
                            .load("http://192.168.178.36:9000/static/img/${prodotto.path_immagine}")
                            .into(binding.immagine)

                        if (prodotto.isPremium() && isUserPremium) {
                            binding.tempoSpedizione.text = "Arriva domani"
                        } else {
                            binding.tempoSpedizione.text = "Arriva fra ${prodotto.tempo_di_spedizione} giorni"
                        }

                        if (prodotto.prezzo_scontato > 0) {
                            binding.prezzoVecchio.text = formatPrice(prodotto.prezzo)
                            binding.prezzoVecchio.visibility = View.VISIBLE
                            binding.prezzoVecchio.paintFlags = binding.prezzoVecchio.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                            binding.sconto.text = "${((1 - (prodotto.prezzo_scontato / prodotto.prezzo)) * 100).toInt()}%"
                            binding.sconto.visibility = View.VISIBLE
                        } else {
                            binding.prezzoVecchio.visibility = View.GONE
                            binding.sconto.visibility = View.GONE
                        }
                    }
                } else {
                    Toast.makeText(this@DettagliProdottoActivity, "Errore nel recupero dei dettagli del prodotto", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<ProdottoItem>, t: Throwable) {
                Toast.makeText(this@DettagliProdottoActivity, "Errore di rete: ${t.message}", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private fun addToCart() {
        try {
            val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("userId", 0)

            if (userId == 0) {
                Toast.makeText(this, "Errore: ID utente non trovato", Toast.LENGTH_SHORT).show()
                return
            }

            val prezzoString = binding.prezzo.text.toString()
            val prezzo = prezzoString.replace("€", "").trim().toDoubleOrNull() ?: 0.0
            val carrelloRequest = JsonObject().apply {
                addProperty("user_id", userId)
                addProperty("prodotto_id", prodottoId)
                addProperty("quantita", 1)
                addProperty("prezzo", prezzo)
            }

            Client.retrofit.aggiungiAlCarrello(carrelloRequest).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DettagliProdottoActivity, "Prodotto aggiunto al carrello", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@DettagliProdottoActivity, NavigationActivity::class.java)
                        intent.putExtra("navigate_to_cart", true)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@DettagliProdottoActivity, "Errore: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@DettagliProdottoActivity, "Errore di rete: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            Toast.makeText(this, "Errore durante l'aggiunta al carrello", Toast.LENGTH_SHORT).show()
        }
    }

    private fun submitReview(prodottoId: Int, reviewText: String, reviewRating: Float) {
        val review = JsonObject().apply {
            addProperty("idprodotto", prodottoId)
            addProperty("idutente", userId)
            addProperty("testo", reviewText)
            addProperty("valutazione", reviewRating)
            val currentTimestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(System.currentTimeMillis())
            addProperty("data_recensione", currentTimestamp)
        }

        Client.retrofit.submitReview(review).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DettagliProdottoActivity, "Recensione inviata con successo", Toast.LENGTH_SHORT).show()
                    reviewViewModel.fetchReviews(prodottoId)
                    reviewViewModel.checkUserReview(prodottoId, userId)
                    binding.boxRecensione.text.clear()
                    binding.recensioneRating.rating = 0f
                } else if (response.code() == 400) {
                    Toast.makeText(this@DettagliProdottoActivity, "Hai già recensito questo prodotto", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DettagliProdottoActivity, "Errore nell'invio della recensione", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@DettagliProdottoActivity, "Errore di rete: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkUserPurchase(prodottoId: Int, userId: Int) {
        val purchaseCheck = JsonObject().apply {
            addProperty("prodotto_id", prodottoId)
            addProperty("user_id", userId)
        }

        Client.retrofit.hasPurchasedProduct(purchaseCheck).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val hasPurchased = response.body()?.get("has_purchased")?.asBoolean ?: false
                    reviewViewModel.setHasPurchased(hasPurchased)
                } else {
                    Toast.makeText(this@DettagliProdottoActivity, "Errore nel controllo dell'acquisto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@DettagliProdottoActivity, "Errore di rete: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}