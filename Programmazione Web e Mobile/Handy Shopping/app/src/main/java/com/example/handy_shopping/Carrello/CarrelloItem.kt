package com.example.handy_shopping.Carrello

data class CarrelloItem(
    val idcarrello: Int,
    val idprodotto: Int,
    val nome: String,
    val prezzo: Double,
    val path_immagine: String,
    var quantita: Int,
    val data_aggiunta: String,
    val prezzo_scontato: Double = 0.0,
    val isDiscounted: Boolean = false
)