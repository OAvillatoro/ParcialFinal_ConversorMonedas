package com.oavillatoro.parcialfinalconversormonedas.data.repository

import com.oavillatoro.parcialfinalconversormonedas.data.remote.RetrofitInstance

class CurrencyRepository {

    private val api = RetrofitInstance.api

    // Convierte una cantidad en USD hacia la moneda seleccionada
    suspend fun convertFromUsd(
        amount: Double,
        targetCurrency: String
    ): Result<Double> {
        return try {
            val response = api.getExchangeRates()

            val rate = response.rates[targetCurrency]

            if (rate != null) {
                val result = amount * rate
                Result.success(result)
            } else {
                Result.failure(
                    Exception("No se encontró el tipo de cambio para $targetCurrency")
                )
            }

        } catch (e: Exception) {
            Result.failure(
                Exception("No hay conexión o la API no respondió correctamente")
            )
        }
    }
}