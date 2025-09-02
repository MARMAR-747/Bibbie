package com.example.handy_shopping.DettagliProdotto

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import android.widget.Toast
import com.example.handy_shopping.R

class ReviewAdapter(
    private var reviews: MutableList<ReviewItem>,
    private val userId: Int,
    private val reviewViewModel: ReviewViewModel
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewText: TextView = itemView.findViewById(R.id.contenuto)
        val reviewUser: TextView = itemView.findViewById(R.id.nomePersona)
        val reviewDate: TextView = itemView.findViewById(R.id.data)
        val reviewRating : RatingBar = itemView.findViewById(R.id.voto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recensione, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.reviewText.text = review.testo
        holder.reviewUser.text = review.nome
        val formattedDate = formatDate(review.data_recensione)
        holder.reviewDate.text = formattedDate
        holder.reviewRating.rating = review.valutazione
        holder.itemView.setOnLongClickListener { view ->
            if (review.idutente == userId) {
                showPopupMenu(view, review, position, holder)
            }
            true
        }
    }

    override fun getItemCount(): Int = reviews.size

    private fun formatDate(dateString: String): String {
        val utcTimeZone = TimeZone.getTimeZone("UTC")
        val localTimeZone = TimeZone.getTimeZone("UTC+2")

        val formats = arrayOf(
            SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH),
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        )

        for (format in formats) {
            try {
                format.timeZone = utcTimeZone
                val date = format.parse(dateString)

                val targetFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH)
                targetFormat.timeZone = localTimeZone
                val formattedDate = targetFormat.format(date)
                return formattedDate
            } catch (e: ParseException) {
                continue
            }
        }
        return dateString
    }

    private fun showPopupMenu(view: View, review: ReviewItem, position: Int, holder: ReviewViewHolder) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_modifica -> {
                    showEditReviewDialog(view.context, review, position, holder)
                    true
                }
                R.id.menu_elimina -> {
                    reviewViewModel.deleteReview(review, userId)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showEditReviewDialog(context: Context, review: ReviewItem, position: Int, holder: ReviewViewHolder) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_review, null)
        val editText = dialogView.findViewById<EditText>(R.id.editTextReview)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBarReview)
        editText.setText(review.testo)
        ratingBar.rating = review.valutazione

        AlertDialog.Builder(context)
            .setTitle("Modifica recensione")
            .setView(dialogView)
            .setPositiveButton("Salva") { dialog, _ ->
                val newText = editText.text.toString()
                val newRating = ratingBar.rating
                if (newText.isNotBlank()) {
                    review.testo = newText
                    review.valutazione = newRating
                    reviewViewModel.updateReview(review)
                } else {
                    Toast.makeText(context, "La recensione non può essere vuota", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Annulla") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    fun updateReviews(newReviews: MutableList<ReviewItem>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}
