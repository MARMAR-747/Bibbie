package com.example.handy_shopping.Carrello

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handy_shopping.R
import com.example.handy_shopping.databinding.FragmentCarrelloBinding

import com.example.handy_shopping.retrofit.Client
import com.google.gson.JsonObject
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarrelloFragment : Fragment(), CarrelloAdapter.TotalUpdateListener {

    private lateinit var binding: FragmentCarrelloBinding
    private lateinit var carrelloAdapter: CarrelloAdapter
    private var userId: Int = 0
    private val carrelloViewModel: CarrelloViewModel by viewModels()
    private var cartItems: MutableList<CarrelloItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarrelloBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences?.getInt("userId", 0) ?: 0


        if (userId == 0) {
            Toast.makeText(context, "Errore: ID utente non trovato", Toast.LENGTH_SHORT).show()
            return
        }

        binding.recyclerViewCarrello.layoutManager = LinearLayoutManager(requireContext())
        carrelloAdapter = CarrelloAdapter(cartItems, requireContext(), userId, this)
        binding.recyclerViewCarrello.adapter = carrelloAdapter
        carrelloViewModel.carrelloItems.observe(viewLifecycleOwner, Observer { items ->
            cartItems.clear()
            cartItems.addAll(items)
            carrelloAdapter.notifyDataSetChanged()
            updateTotal()
        })

        carrelloViewModel.fetchCartItems(userId)

        binding.buttonAcquistaTutto.setOnClickListener {
            if (cartItems.isNotEmpty()) {
                if (isBillingInfoComplete()) {
                    showPaymentDialogForAllItems()
                } else {
                    Toast.makeText(context, "Dati di fatturazione assenti, aggiorna i tuoi dati.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Il carrello è vuoto.", Toast.LENGTH_SHORT).show()
            }
        }
        setupAutoRefresh()
    }

    //funzioni
    private fun isBillingInfoComplete(): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val citta = sharedPreferences.getString("citta", "")
        val indirizzo = sharedPreferences.getString("indirizzo", "")
        val numeroCivico = sharedPreferences.getString("numeroCivico", "")
        return !citta.isNullOrEmpty() && !indirizzo.isNullOrEmpty() && !numeroCivico.isNullOrEmpty()
    }


    private fun setupAutoRefresh() {
        val refreshInterval = 60000L // 1 minuto
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            while (true) {
                carrelloViewModel.fetchCartItems(userId)
                delay(refreshInterval)
            }
        }
    }


    override fun onTotalUpdated(total: Double) {
        binding.tvTotaleCarrello.text = "Totale: €${String.format("%.2f", total)}"
    }


    private fun updateTotal() {
        val total = cartItems.sumByDouble { item ->
            if (item.isDiscounted) (item.prezzo / 2) * item.quantita else item.prezzo * item.quantita
        }
        onTotalUpdated(total)
    }

    private fun showPaymentDialogForAllItems() {
        val dialog = Dialog(requireContext())
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
                    val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    val cardNumber = sharedPreferences.getString("cardNumber", "")
                    val expirationDate = sharedPreferences.getString("expirationDate", "")
                    val cvv = sharedPreferences.getString("cvv", "")
                    if (cardNumber.isNullOrEmpty() || expirationDate.isNullOrEmpty() || cvv.isNullOrEmpty()) {
                        Toast.makeText(context, "Carta di credito assente, aggiorna i tuoi dati.", Toast.LENGTH_SHORT).show()
                    } else {
                        dialog.dismiss()
                        showCartaDialogForAllItems(paymentMethod)
                    }
                } else {
                    dialog.dismiss()
                    purchaseAllItems(paymentMethod)
                }
            } else {
                Toast.makeText(context, "Per favore, seleziona un metodo di pagamento.", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun showCartaDialogForAllItems(paymentMethod: String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.card_choice_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        val radioGroup: RadioGroup = dialog.findViewById(R.id.radioGroupInstallment)
        val slider: SeekBar = dialog.findViewById(R.id.seekBarInstallment)
        val sliderValue: TextView = dialog.findViewById(R.id.sliderValue)
        slider.max = 4
        slider.progress = 1
        sliderValue.text = "2 rate da ${formatPrice(calculateTotalInstallment(2))}"
        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sliderValue.text = "$progress rate da ${formatPrice(calculateTotalInstallment(progress))}"
                if (progress > 1) {
                    radioGroup.check(R.id.radioInstallment)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val btnAnnulla: Button = dialog.findViewById(R.id.btnAnnulla)
        val btnConferma: Button = dialog.findViewById(R.id.btnConferma)

        btnAnnulla.setOnClickListener {
            dialog.dismiss()
        }

        btnConferma.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton: RadioButton = dialog.findViewById(selectedRadioButtonId)
                val installmentMethod = selectedRadioButton.text.toString()
                if (installmentMethod == "Pagamento unico") {
                    purchaseAllItems(paymentMethod)
                } else {
                    val installmentCount = slider.progress
                    purchaseAllItems(paymentMethod, installmentCount)
                }
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Per favore, seleziona un'opzione di pagamento.", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun calculateTotalInstallment(rateCount: Int): Double {
        val total = cartItems.sumByDouble { item ->
            if (item.isDiscounted) (item.prezzo / 2) * item.quantita else item.prezzo * item.quantita
        }
        return total / rateCount
    }

    private fun formatPrice(price: Double): String {
        return if (price == price.toInt().toDouble()) {
            "€${price.toInt()}"
        } else {
            String.format("€%.2f", price)
        }
    }

    //retrofit
    private fun purchaseAllItems(paymentMethod: String, rate: Int = 1) {
        var totalSpent = 0.0
        cartItems.forEach { item ->
            val itemTotal = if (item.isDiscounted) (item.prezzo / 2) * item.quantita else item.prezzo * item.quantita
            totalSpent += itemTotal
            val orderRequest = JsonObject().apply {
                addProperty("user_id", userId)
                addProperty("product_id", item.idprodotto)
                addProperty("quantity", item.quantita)
                addProperty("total", itemTotal)
                addProperty("isDiscounted", item.isDiscounted)
                addProperty("payment_method", paymentMethod)
                addProperty("rate", rate)
            }
            Client.retrofit.addOrder(orderRequest).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        deleteCartItem(item)
                    } else {
                        Toast.makeText(context, "Errore: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(context, "Errore di rete: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        cartItems.clear()
        carrelloAdapter.notifyDataSetChanged()
        updateTotal()
        Toast.makeText(context, "Acquisto completato.", Toast.LENGTH_SHORT).show()
    }


    private fun deleteCartItem(item: CarrelloItem) {
        val request = JsonObject().apply {
            addProperty("idcarrello", item.idcarrello)
        }
        Client.retrofit.deleteCarrelloItem(request).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (!response.isSuccessful) {
                    Toast.makeText(context, "Errore durante la rimozione: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore di rete durante la rimozione: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
