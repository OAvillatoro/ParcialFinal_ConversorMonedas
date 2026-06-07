package com.oavillatoro.parcialfinalconversormonedas.data.model

data class ExchangeRatesResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)