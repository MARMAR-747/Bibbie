package com.example.handy_shopping.DettagliProdotto

data class ReviewItem(
    val idrecensione: Int,
    val idprodotto: Int,
    val idutente: Int,
    var testo: String,
    val nome: String,
    var valutazione: Float,
    var data_recensione: String
)
