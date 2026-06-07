package com.oavillatoro.parcialfinalconversormonedas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oavillatoro.parcialfinalconversormonedas.data.model.CurrencyOption
import com.oavillatoro.parcialfinalconversormonedas.data.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CurrencyUiState {
    object Initial : CurrencyUiState()
    object Loading : CurrencyUiState()
    data class Success(val result: Double) : CurrencyUiState()
    data class Error(val message: String) : CurrencyUiState()
}

class CurrencyViewModel : ViewModel() {

    private val repository = CurrencyRepository()

    val currencies = listOf(
        CurrencyOption("GTQ", "Quetzales guatemaltecos"),
        CurrencyOption("HNL", "Lempiras hondureñas"),
        CurrencyOption("NIO", "Córdobas nicaragüenses"),
        CurrencyOption("MXN", "Pesos mexicanos")
    )

    private val _uiState = MutableStateFlow<CurrencyUiState>(CurrencyUiState.Initial)
    val uiState: StateFlow<CurrencyUiState> = _uiState

    fun convert(amountText: String, targetCurrency: CurrencyOption) {
        val amount = amountText.toDoubleOrNull()

        if (amount == null || amount <= 0) {
            _uiState.value = CurrencyUiState.Error(
                "Ingrese una cantidad válida en dólares."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = CurrencyUiState.Loading

            repository.convertFromUsd(
                amount = amount,
                targetCurrency = targetCurrency.code
            ).onSuccess { result ->
                _uiState.value = CurrencyUiState.Success(result)
            }.onFailure { error ->
                _uiState.value = CurrencyUiState.Error(
                    error.message ?: "Ocurrió un error inesperado."
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = CurrencyUiState.Initial
    }
}