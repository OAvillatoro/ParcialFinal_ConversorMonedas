package com.oavillatoro.parcialfinalconversormonedas.data.remote

import com.oavillatoro.parcialfinalconversormonedas.data.model.ExchangeRatesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // Endpoint para obtener tasas de cambio desde una moneda base.
    // En este proyecto usamos USD como moneda base.
    @GET("v6/latest/{baseCurrency}")
    suspend fun getExchangeRates(
        @Path("baseCurrency") baseCurrency: String = "USD"
    ): ExchangeRatesResponse
}