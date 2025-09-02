package com.example.handy_shopping.Home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handy_shopping.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var listener: OnFragmentInteractionListener? = null
    private var isUserPremium: Boolean = false
    private lateinit var prodottoAdapter: ProdottoAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private val refreshInterval = 10000L // 10 secondi

    interface OnFragmentInteractionListener {
        fun onSwitchFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewVetrina.layoutManager = LinearLayoutManager(requireContext())
        prodottoAdapter = ProdottoAdapter(emptyList())
        binding.recyclerViewVetrina.adapter = prodottoAdapter

        val sharedPreferences = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        isUserPremium = sharedPreferences?.getBoolean("isPremium", false) ?: false
        prodottoAdapter.setUserPremium(isUserPremium)

        homeViewModel.discountedProducts.observe(viewLifecycleOwner, Observer { prodotti ->
            prodottoAdapter.updateProdotti(prodotti)
        })

        homeViewModel.fetchDiscountedProducts()

        // binding bottoni
        binding.searchIcon.setOnClickListener {
            listener?.onSwitchFragment()
        }

        startAutoRefresh()
    }

    //funzione di refresh
    private fun startAutoRefresh() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                homeViewModel.fetchDiscountedProducts()
                handler.postDelayed(this, refreshInterval)
            }
        }, refreshInterval)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        handler.removeCallbacksAndMessages(null)
    }
}

