package com.example.handy_shopping.Carrello

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.handy_shopping.DettagliProdotto.DettagliProdottoActivity
import com.example.handy_shopping.R
import com.example.handy_shopping.retrofit.Client
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarrelloAdapter(
    private var cartItems: MutableList<CarrelloItem>,
    private val context: Context,
    private val userId: Int,
    private val totalUpdateListener: TotalUpdateListener
) : RecyclerView.Adapter<CarrelloAdapter.CarrelloViewHolder>() {

    interface TotalUpdateListener {
        fun onTotalUpdated(total: Double)
    }

    class CarrelloViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val immagine: ImageView = itemView.findViewById(R.id.immagine)
        val titolo: TextView = itemView.findViewById(R.id.titolo)
        val prezzo: TextView = itemView.findViewById(R.id.prezzo)
        val quantita: TextView = itemView.findViewById(R.id.quantita)
        val btnMinus: ImageView = itemView.findViewById(R.id.minus)
        val btnPlus: ImageView = itemView.findViewById(R.id.plus)
        val btnDelete: ImageView = itemView.findViewById(R.id.delete)
        val btnAcquista: Button = itemView.findViewById(R.id.btnAcquista)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarrelloViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_cart, parent, false)
        return CarrelloViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarrelloViewHolder, position: Int) {
        val item = cartItems[position]


        holder.titolo.text = item.nome
        val prezzo = if (item.prezzo_scontato > 0) item.prezzo_scontato else item.prezzo
        holder.prezzo.text = formatPrice(prezzo)
        holder.quantita.text = item.quantita.toString()
        Glide.with(holder.itemView.context)
            .load("http://192.168.178.36:9000/static/img/${item.path_immagine}")
            .into(holder.immagine)


        holder.btnMinus.setOnClickListener {
            updateQuantity(item, holder.quantita, holder.prezzo, -1)
        }

        holder.btnPlus.setOnClickListener {
            updateQuantity(item, holder.quantita, holder.prezzo, 1)
        }

        holder.btnDelete.setOnClickListener {
            deleteCartItem(item, position)
        }

        holder.btnAcquista.setOnClickListener {
            val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val citta = sharedPreferences.getString("citta", "")
            val indirizzo = sharedPreferences.getString("indirizzo", "")
            val numeroCivico = sharedPreferences.getString("numeroCivico", "")

            if (citta.isNullOrEmpty() || indirizzo.isNullOrEmpty() || numeroCivico.isNullOrEmpty()) {
                Toast.makeText(context, "Dati di fatturazione assenti, aggiorna i tuoi dati.", Toast.LENGTH_SHORT).show()
            } else {
                showPaymentDialog(item, position)
            }
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DettagliProdottoActivity::class.java)
            intent.putExtra("prodotto_id", item.idprodotto)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = cartItems.size

    //funzioni
    private fun updatePrice(priceView: TextView, price: Double, quantity : Int, discountedPrice: Double) {
        val newPrice = if (discountedPrice > 0) discountedPrice * quantity else price * quantity
        priceView.text = formatPrice(newPrice)
    }

    private fun showPaymentDialog(item: CarrelloItem, position: Int) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.payment_choice_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        val radioGroup: RadioGroup = dialog.findViewById(R.id.radioGroup)
        val btnAnnulla: Button = dialog.findViewById(R.id.btnAnnulla)
        val btnConferma: Button = dialog.findViewById(R.id.btnConferma)
        btnAnnulla.setOnClickListener {
            dialog.dismiss()
        }
        btnConferma.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton: RadioButton = dialog.findViewById(selectedRadioButtonId)
                val paymentMethod = selectedRadioButton.text.toString()
                if (paymentMethod == "Carta di credito") {
                    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    val cardNumber = sharedPreferences.getString("cardNumber", "")
                    val expirationDate = sharedPreferences.getString("expirationDate", "")
                    val cvv = sharedPreferences.getString("cvv", "")
                    if (cardNumber.isNullOrEmpty() || expirationDate.isNullOrEmpty() || cvv.isNullOrEmpty()) {
                        Toast.makeText(context, "Carta di credito assente, aggiorna i tuoi dati.", Toast.LENGTH_SHORT).show()
                    } else {
                        dialog.dismiss()
                        showCartaDialog(item, position, paymentMethod)
                    }
                } else {
                    dialog.dismiss()
                    purchaseItem(item, position, paymentMethod)
                }
            } else {
                Toast.makeText(context, "Per favore, seleziona un metodo di pagamento.", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun showCartaDialog(item: CarrelloItem, position: Int, paymentMethod: String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.card_choice_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        val radioGroup: RadioGroup = dialog.findViewById(R.id.radioGroupInstallment)
        val slider: SeekBar = dialog.findViewById(R.id.seekBarInstallment)
        val sliderValue: TextView = dialog.findViewById(R.id.sliderValue)
        val btnAnnulla: Button = dialog.findViewById(R.id.btnAnnulla)
        val btnConferma: Button = dialog.findViewById(R.id.btnConferma)
        slider.max = 4
        slider.progress = 1
        sliderValue.text = "2 rate da ${formatPrice(calculateInstallment(item, 2))}"
        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sliderValue.text = progress.toString()
                if (progress > 1) {
                    radioGroup.check(R.id.radioInstallment)
                }
                sliderValue.text = "$progress rate da ${formatPrice(calculateInstallment(item, progress))}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnAnnulla.setOnClickListener {
            dialog.dismiss()
        }

        btnConferma.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton: RadioButton = dialog.findViewById(selectedRadioButtonId)
                val installmentMethod = selectedRadioButton.text.toString()
                if (installmentMethod == "Pagamento unico") {
                    purchaseItem(item, position, paymentMethod)
                } else {
                    val installmentCount = slider.progress
                    purchaseItem(item, position, paymentMethod, installmentCount)
                }
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Per favore, seleziona un'opzione di pagamento.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun calculateInstallment(item: CarrelloItem, rateCount: Int): Double {
        val total = if (item.prezzo_scontato > 0) item.prezzo_scontato * item.quantita else item.prezzo * item.quantita
        return total / rateCount
    }


    private fun updateTotal() {
        val total = cartItems.sumByDouble { item ->
            if (item.prezzo_scontato > 0) item.prezzo_scontato * item.quantita else item.prezzo * item.quantita
        }
        totalUpdateListener.onTotalUpdated(total)
    }


    private fun formatPrice(price: Double): String {
        return if (price == price.toInt().toDouble()) {
            "€${price.toInt()}"
        } else {
            String.format("€%.2f", price)
        }
    }

    //retrofit
    private fun updateQuantity(item: CarrelloItem, quantityView: TextView, priceView : TextView, delta: Int) {
        val newQuantity = item.quantita + delta

        if (newQuantity <= 0) {
            deleteCartItem(item, cartItems.indexOf(item))
        } else if (newQuantity <= 10) {
            item.quantita = newQuantity
            quantityView.text = newQuantity.toString()
            updatePrice(priceView, item.prezzo, newQuantity, item.prezzo_scontato)
            updateTotal()
            val request = JsonObject().apply {
                addProperty("idcarrello", item.idcarrello)
                addProperty("quantita", newQuantity)
            }
            Client.retrofit.updateCartItemQuantity(request).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    response.isSuccessful
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                }
            })
            updateTotal()
        }
    }

    private fun deleteCartItem(item: CarrelloItem, position: Int) {
        val request = JsonObject().apply {
            addProperty("idcarrello", item.idcarrello)
        }
        Client.retrofit.deleteCarrelloItem(request).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    cartItems.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cartItems.size)
                    updateTotal()
                } else {
                    Toast.makeText(context, "Errore: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore di rete: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun purchaseItem(item: CarrelloItem, position: Int, paymentMethod: String, rate: Int = 1) {
        Client.retrofit.getProductPrice(item.idprodotto).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val prezzoCorrente = response.body()?.get("prezzo")?.asDouble ?: item.prezzo
                    val prezzoScontatoCorrente = response.body()?.get("prezzo_scontato")?.asDouble ?: item.prezzo_scontato
                    val total = if (prezzoScontatoCorrente > 0) prezzoScontatoCorrente * item.quantita else prezzoCorrente * item.quantita

                    val orderRequest = JsonObject().apply {
                        addProperty("user_id", userId)
                        addProperty("product_id", item.idprodotto)
                        addProperty("quantity", item.quantita)
                        addProperty("total", total)
                        addProperty("payment_method", paymentMethod)
                        addProperty("rate", rate)
                    }

                    Client.retrofit.addOrder(orderRequest).enqueue(object : Callback<JsonObject> {
                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                            if (response.isSuccessful) {
                                deleteCartItem(item, position)
                            } else {
                                Toast.makeText(context, "Errore: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            Toast.makeText(context, "Errore di rete: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(context, "Errore nel recupero del prezzo corrente", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore di rete: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
