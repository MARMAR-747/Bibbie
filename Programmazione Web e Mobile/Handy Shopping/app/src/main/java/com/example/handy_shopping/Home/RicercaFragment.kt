package com.example.handy_shopping.Home

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handy_shopping.databinding.FragmentRicercaBinding
import com.example.handy_shopping.retrofit.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RicercaFragment : Fragment() {

    private lateinit var binding: FragmentRicercaBinding
    private lateinit var prodottoAdapter: ProdottoAdapter
    private var prodotti: List<ProdottoItem> = listOf()
    private var selectedCategories: MutableList<String> = mutableListOf()
    private var minPrice: Float = 0f
    private var maxPrice: Float = 1000f
    private val upperLimit: Float = 1000f
    private val upperLimitLabel = "1000+"
    private var isUserPremium: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRicercaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        isUserPremium = sharedPreferences?.getBoolean("isPremium", false) ?: false

        binding.recyclerViewRicerca.layoutManager = LinearLayoutManager(requireContext())
        prodottoAdapter = ProdottoAdapter(prodotti)
        binding.recyclerViewRicerca.adapter = prodottoAdapter

        //Se l'utente è premium lo mostro
        prodottoAdapter.setUserPremium(isUserPremium)

        //binding bottoni
        binding.boxRicerca.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchProducts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.checkElettronica.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedCategories.add("Elettronica") else selectedCategories.remove("Elettronica")
            searchProducts(binding.boxRicerca.text.toString())
        }
        binding.checkGiocattoli.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedCategories.add("Giocattoli") else selectedCategories.remove("Giocattoli")
            searchProducts(binding.boxRicerca.text.toString())
        }
        binding.checkAltro.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedCategories.add("Altro") else selectedCategories.remove("Altro")
            searchProducts(binding.boxRicerca.text.toString())
        }

        binding.priceRangeSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            minPrice = values[0]
            maxPrice = if (values[1] == upperLimit) Float.MAX_VALUE else values[1]
            searchProducts(binding.boxRicerca.text.toString())
        }

        binding.priceRangeSlider.setLabelFormatter { value ->
            if (value == upperLimit) {
                upperLimitLabel
            } else {
                value.toInt().toString()
            }
        }
    }

    //funzioni con retrofit
    private fun searchProducts(query: String) {
        Client.retrofit.searchProducts(query, selectedCategories, minPrice, maxPrice).enqueue(object : Callback<List<ProdottoItem>> {
            override fun onResponse(call: Call<List<ProdottoItem>>, response: Response<List<ProdottoItem>>) {
                if (response.isSuccessful) {
                    prodotti = response.body() ?: listOf()
                    prodottoAdapter.updateProdotti(prodotti)
                } else {
                    Toast.makeText(context, "Errore: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ProdottoItem>>, t: Throwable) {
                Toast.makeText(context, "Errore di rete: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
