package com.example.calculatorcourse.domain

sealed class CalculatorAction {
    data class Number(val value: Int) : CalculatorAction()
    data object Decimal : CalculatorAction()
    data object Clear : CalculatorAction()
    data object Delete : CalculatorAction()
    data object Calculate : CalculatorAction()
    data object Add : CalculatorAction()
    data object Subtract : CalculatorAction()
    data object Multiply : CalculatorAction()
    data object Divide : CalculatorAction()
}
