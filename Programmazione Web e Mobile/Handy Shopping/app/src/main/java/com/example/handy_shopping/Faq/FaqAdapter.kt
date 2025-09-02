package com.example.handy_shopping.Faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.handy_shopping.R

class FaqAdapter(
    private val faqItemList: List<FaqItem>,
    private val onLastItemClickListener: (Boolean) -> Unit
) : RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    inner class FaqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val domanda: TextView = itemView.findViewById(R.id.domanda)
        val risposta: TextView = itemView.findViewById(R.id.risposta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_faq, parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val faq = faqItemList[position]
        holder.domanda.text = faq.question
        holder.risposta.text = faq.answer

        holder.domanda.setOnClickListener {
            if (holder.risposta.visibility == View.GONE) {
                holder.risposta.visibility = View.VISIBLE
            } else {
                holder.risposta.visibility = View.GONE
            }
            if (position == faqItemList.size - 1) {
                onLastItemClickListener(holder.risposta.visibility == View.VISIBLE)
            }
        }
    }

    override fun getItemCount() = faqItemList.size
}