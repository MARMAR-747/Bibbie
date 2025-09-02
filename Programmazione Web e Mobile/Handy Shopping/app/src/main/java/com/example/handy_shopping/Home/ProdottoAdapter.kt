package com.example.handy_shopping.Home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.handy_shopping.retrofit.Client
import com.example.handy_shopping.DettagliProdotto.DettagliProdottoActivity
import com.example.handy_shopping.R
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdottoAdapter(
    private var prodotti: List<ProdottoItem>
) : RecyclerView.Adapter<ProdottoAdapter.ProdottoViewHolder>() {

    private var isUserPremium: Boolean = false

    class ProdottoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val immagine: ImageView = itemView.findViewById(R.id.immagine)
        val titolo: TextView = itemView.findViewById(R.id.titolo)
        val prezzo: TextView = itemView.findViewById(R.id.prezzo)
        val premium: TextView = itemView.findViewById(R.id.premium)
        val voto: RatingBar = itemView.findViewById(R.id.voto)
        val tempoSpedizione: TextView = itemView.findViewById(R.id.tempoSpedizione)
        val btnAcquista: ImageView = itemView.findViewById(R.id.btnAcquista)
        val sconto: TextView = itemView.findViewById(R.id.sconto)
        val prezzoVecchio = itemView.findViewById<TextView>(R.id.prezzoVecchio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdottoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_search, parent, false)
        return ProdottoViewHolder(view)

    }

    override fun onBindViewHolder(holder: ProdottoViewHolder, position: Int) {
        val prodotto = prodotti[position]


        holder.titolo.text = prodotto.nome
        holder.prezzo.text = formatPrice(if (prodotto.prezzo_scontato > 0) prodotto.prezzo_scontato else prodotto.prezzo)
        holder.voto.rating = prodotto.valutazione
        holder.prezzoVecchio.paintFlags = holder.prezzoVecchio.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
        holder.prezzoVecchio.text = formatPrice(prodotto.prezzo)

        if (prodotto.isPremium()) {
            holder.premium.visibility = View.VISIBLE
            if (isUserPremium) {
                holder.tempoSpedizione.text = "Arriva domani"
            } else {
                holder.tempoSpedizione.text = "Arriva fra ${prodotto.tempo_di_spedizione} giorni"
            }
        } else {
            holder.premium.visibility = View.GONE
            holder.tempoSpedizione.text = "Arriva fra ${prodotto.tempo_di_spedizione} giorni"
        }

        Glide.with(holder.itemView.context)
            .load("http://192.168.178.36:9000/static/img/${prodotto.path_immagine}")
            .into(holder.immagine)

        if (prodotto.prezzo_scontato > 0) {
            holder.sconto.text = "${((1 - (prodotto.prezzo_scontato / prodotto.prezzo)) * 100).toInt()}%"
            holder.sconto.visibility = View.VISIBLE
            holder.prezzoVecchio.visibility = View.VISIBLE
        } else {
            holder.prezzoVecchio.visibility = View.GONE
            holder.sconto.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DettagliProdottoActivity::class.java)
            intent.putExtra("prodotto_id", prodotto.idprodotto)
            context.startActivity(intent)
        }

        holder.btnAcquista.setOnClickListener {
            val context = holder.itemView.context
            val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("userId", 0)

            if (userId == 0) {
                Toast.makeText(context, "Errore: ID utente non trovato", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val carrelloRequest = JsonObject().apply {
                addProperty("user_id", userId)
                addProperty("prodotto_id", prodotto.idprodotto)
                addProperty("quantita", 1)
                addProperty("prezzo", if (prodotto.prezzo_scontato > 0) prodotto.prezzo_scontato else prodotto.prezzo)
            }

            Client.retrofit.aggiungiAlCarrello(carrelloRequest).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Prodotto aggiunto al carrello", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Errore: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(context, "Errore di rete: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun getItemCount(): Int = prodotti.size

    fun setUserPremium(isPremium: Boolean) {
        this.isUserPremium = isPremium
        notifyDataSetChanged()
    }

    fun updateProdotti(prodotti: List<ProdottoItem>) {
        this.prodotti = prodotti
        notifyDataSetChanged()
    }

    private fun formatPrice(price: Double): String {
        return if (price == price.toInt().toDouble()) {
            "€${price.toInt()}"
        } else {
            String.format("€%.2f", price)
        }
    }
}
