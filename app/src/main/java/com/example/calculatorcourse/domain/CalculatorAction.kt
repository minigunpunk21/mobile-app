package com.example.calculatorcourse.domain

sealed interface CalculatorAction {
    data class Number(val value: Int) : CalculatorAction
    data object Decimal : CalculatorAction
    data object Clear : CalculatorAction
    data object Delete : CalculatorAction
    data object Calculate : CalculatorAction
    data object ToggleSign : CalculatorAction
    data object Percent : CalculatorAction
    data class Operation(val value: CalculatorOperation) : CalculatorAction
}
