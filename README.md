# ParcialFinal_ConversorMonedas

Aplicación móvil desarrollada en Android Studio utilizando Kotlin y Jetpack Compose para convertir dólares estadounidenses USD a diferentes monedas de la región.

## Descripción

La aplicación permite ingresar una cantidad en dólares USD, seleccionar una moneda destino y obtener el resultado de la conversión usando un tipo de cambio actualizado desde una API REST.

## Monedas disponibles

La aplicación permite convertir USD a:

- Quetzales guatemaltecos (GTQ)
- Lempiras hondureñas (HNL)
- Córdobas nicaragüenses (NIO)
- Pesos mexicanos (MXN)

## API utilizada

Se utilizó ExchangeRate-API mediante el siguiente endpoint abierto:

```text
https://open.er-api.com/v6/latest/USD
