package com.oavillatoro.parcialfinalconversormonedas.data.remote

import com.oavillatoro.parcialfinalconversormonedas.data.model.ExchangeRatesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Endpoint para obtener tasas de cambio desde USD hacia monedas específicas
    @GET("v2/rates")
    suspend fun getExchangeRates(
        @Query("base") base: String = "USD",
        @Query("quotes") quotes: String = "GTQ,HNL,NIO,MXN"
    ): ExchangeRatesResponse
}