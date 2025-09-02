package com.example.handy_shopping.Acquisti

data class AcquistoItem(
    val idordine: Int,
    val idutente: Int,
    val idprodotto: Int,
    val nome: String,
    val quantita: Int,
    val data_ordine: String,
    val totale: Double,
    val stato: Int,
    val stelle: Float,
    val isDiscounted: Boolean
)
