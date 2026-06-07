package com.oavillatoro.parcialfinalconversormonedas.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oavillatoro.parcialfinalconversormonedas.data.model.CurrencyOption
import com.oavillatoro.parcialfinalconversormonedas.ui.viewmodel.CurrencyUiState
import com.oavillatoro.parcialfinalconversormonedas.ui.viewmodel.CurrencyViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen(
    viewModel: CurrencyViewModel = viewModel()
) {
    var amountText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCurrency by remember {
        mutableStateOf(viewModel.currencies.first())
    }

    val uiState by viewModel.uiState.collectAsState()
    val decimalFormat = DecimalFormat("#,##0.00")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Conversor de Monedas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Convierte dólares estadounidenses USD a monedas de la región.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = {
                amountText = it
                viewModel.clearMessage()
            },
            label = { Text("Cantidad en dólares USD") },
            placeholder = { Text("Ejemplo: 25.50") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = "${selectedCurrency.name} (${selectedCurrency.code})",
                onValueChange = {},
                readOnly = true,
                label = { Text("Moneda destino") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                viewModel.currencies.forEach { currency ->
                    DropdownMenuItem(
                        text = {
                            Text("${currency.name} (${currency.code})")
                        },
                        onClick = {
                            selectedCurrency = currency
                            expanded = false
                            viewModel.clearMessage()
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.convert(
                    amountText = amountText,
                    targetCurrency = selectedCurrency
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convertir")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                amountText = ""
                selectedCurrency = viewModel.currencies.first()
                viewModel.clearMessage()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (uiState) {
            is CurrencyUiState.Initial -> {
                Text(
                    text = "Ingrese una cantidad y seleccione una moneda.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            is CurrencyUiState.Loading -> {
                CircularProgressIndicator()

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Consultando tipo de cambio actualizado...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            is CurrencyUiState.Success -> {
                val result = (uiState as CurrencyUiState.Success).result

                ResultCard(
                    amountText = amountText,
                    selectedCurrency = selectedCurrency,
                    resultText = decimalFormat.format(result)
                )
            }

            is CurrencyUiState.Error -> {
                Text(
                    text = (uiState as CurrencyUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ResultCard(
    amountText: String,
    selectedCurrency: CurrencyOption,
    resultText: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Resultado de la conversión",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$amountText USD equivalen aproximadamente a:"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$resultText ${selectedCurrency.code}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = selectedCurrency.name,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}