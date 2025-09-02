package com.example.handy_shopping.Acquisti

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RatingBar
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class AcquistoAdapter(
    private var AcquistoItem: List<AcquistoItem>,
    private val userId: Int
) : RecyclerView.Adapter<AcquistoAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val immagine: ImageView = itemView.findViewById(R.id.immagine)
        val titolo: TextView = itemView.findViewById(R.id.titolo)
        val prezzo: TextView = itemView.findViewById(R.id.prezzo)
        val data: TextView = itemView.findViewById(R.id.data)
        val quantita: TextView = itemView.findViewById(R.id.unitaAcquistate)
        val voto: RatingBar = itemView.findViewById(R.id.voto)
        val btnReso: Button = itemView.findViewById(R.id.btnAcquista)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_log, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = AcquistoItem[position]

        holder.titolo.text = item.nome
        val prezzo = if (item.isDiscounted) item.totale / 2 else item.totale
        holder.prezzo.text = formatPrice(prezzo)
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        try {
            val date = dateFormat.parse(item.data_ordine)
            val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
            holder.data.text = "Acquistato il $formattedDate"
        } catch (e: ParseException) {
            holder.data.text = "Data non disponibile"
        }

        holder.quantita.text = "Unità acquistate: ${item.quantita}"

        Log.d("AcquistoAdapter", "Item: ${item.nome}, isDiscounted: ${item.isDiscounted}, Prezzo: $prezzo")

        Glide.with(holder.itemView.context)
            .load("http://192.168.178.36:9000/static/img/${item.idprodotto}.jpg")
            .into(holder.immagine)

        holder.voto.rating = item.stelle

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DettagliProdottoActivity::class.java)
            intent.putExtra("prodotto_id", item.idprodotto)
            context.startActivity(intent)
        }

        holder.btnReso.setOnClickListener {

            try {
                val orderDate = dateFormat.parse(item.data_ordine)
                val currentDate = Date()
                val currentDatePlus2Hours = Date(currentDate.time + TimeUnit.HOURS.toMillis(2))
                val diffInMillis = currentDatePlus2Hours.time - orderDate.time
                val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                if (diffInDays < 30) {
                    showResoDialog(holder.itemView.context, item, position)
                } else {
                    Toast.makeText(holder.itemView.context,"Tempo scaduto per il reso.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ParseException) {
                Toast.makeText(holder.itemView.context, "Errore nel parsing della data.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = AcquistoItem.size

    //funzioni
    private fun formatPrice(price: Double): String {
        return if (price == price.toInt().toDouble()) {
            "€${price.toInt()}"
        } else {
            String.format("€%.2f", price)
        }
    }

    private fun showResoDialog(context: Context, item: AcquistoItem, position: Int) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.return_reason_dialog)
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
                dialog.dismiss()
                requestReso(item, position, context)
            } else {
                Toast.makeText(context, "Per favore, seleziona un motivo per il reso.", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    //funzioni con retrofit
    private fun requestReso(item: AcquistoItem, position: Int, context: Context) {
        val request = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("order_id", item.idordine)
        }
        Client.retrofit.deleteOrder(request).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    (AcquistoItem as MutableList).removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, AcquistoItem.size)
                    Toast.makeText(context, "Reso effettuato con successo.", Toast.LENGTH_SHORT).show()
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
