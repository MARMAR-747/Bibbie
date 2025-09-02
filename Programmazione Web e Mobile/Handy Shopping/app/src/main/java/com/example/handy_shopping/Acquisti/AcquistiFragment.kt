package com.example.handy_shopping.Acquisti


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handy_shopping.databinding.FragmentAcquistiBinding
import com.example.handy_shopping.retrofit.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AcquistiFragment : Fragment() {

    private lateinit var binding: FragmentAcquistiBinding
    private lateinit var orderAdapter: AcquistoAdapter
    private var userId: Int = 0
    private var orderItems: MutableList<AcquistoItem> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAcquistiBinding.inflate(inflater, container, false)
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

        binding.recyclerViewAcquisti.layoutManager = LinearLayoutManager(requireContext())
        orderAdapter = AcquistoAdapter(orderItems, userId)
        binding.recyclerViewAcquisti.adapter = orderAdapter


        fetchOrderItems()
    }


    private fun fetchOrderItems() {
        Client.retrofit.getOrders(userId).enqueue(object : Callback<List<AcquistoItem>> {
            override fun onResponse(call: Call<List<AcquistoItem>>, response: Response<List<AcquistoItem>>) {
                if (response.isSuccessful) {
                    val newItems = response.body() ?: listOf()
                    val oldSize = orderItems.size
                    orderItems.clear()
                    orderItems.addAll(newItems)
                    if (oldSize == 0) {
                        orderAdapter.notifyItemRangeInserted(0, newItems.size)
                    } else {
                        orderAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(context, "Errore: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<AcquistoItem>>, t: Throwable) {
                Toast.makeText(context, "Errore di rete: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

