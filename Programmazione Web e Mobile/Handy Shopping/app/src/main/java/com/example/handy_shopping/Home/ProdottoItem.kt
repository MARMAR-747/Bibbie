package com.example.handy_shopping.Home

data class ProdottoItem(
    val idprodotto: Int,
    val categoria: String,
    val marca: String,
    val nome: String,
    val prezzo: Double,
    val valutazione: Float,
    val tempo_di_spedizione: Int,
    val path_immagine: String,
    val premium : Any,
    var isDiscounted: Boolean,
    var prezzo_scontato: Double,
    val sconto : Int = 0,
    val descrizione: String

) {
    fun isPremium(): Boolean {
        return when (premium) {
            is Boolean -> premium
            is Number -> premium.toInt() != 0
            else -> false
        }
    }
}