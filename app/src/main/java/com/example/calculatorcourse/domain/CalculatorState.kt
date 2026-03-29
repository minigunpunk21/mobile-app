package com.example.calculatorcourse.domain

data class CalculatorState(
    val display: String = "0",
    val expression: String = "",
    val hasError: Boolean = false
)
