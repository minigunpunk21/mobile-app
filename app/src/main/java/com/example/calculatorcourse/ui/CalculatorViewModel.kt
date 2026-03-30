package com.example.calculatorcourse.ui

import androidx.lifecycle.ViewModel
import com.example.calculatorcourse.domain.CalculatorAction
import com.example.calculatorcourse.domain.CalculatorEngine
import com.example.calculatorcourse.domain.CalculatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel : ViewModel() {

    private val engine = CalculatorEngine()

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> append(action.value.toString())
            CalculatorAction.Decimal -> appendDecimal()
            CalculatorAction.Clear -> clear()
            CalculatorAction.Delete -> delete()
            CalculatorAction.Calculate -> calculate()
            CalculatorAction.Add -> appendOperator("+")
            CalculatorAction.Subtract -> appendOperator("-")
            CalculatorAction.Multiply -> appendOperator("*")
            CalculatorAction.Divide -> appendOperator("/")
        }
    }

    fun clear() {
        _state.value = CalculatorState()
    }

    private fun append(value: String) {
        val newExpression = _state.value.expression + value
        _state.value = _state.value.copy(
            expression = newExpression,
            result = engine.evaluate(newExpression)
        )
    }

    private fun appendDecimal() {
        val expr = _state.value.expression
        val lastPart = expr.takeLastWhile { it.isDigit() || it == '.' }

        if (!lastPart.contains(".")) {
            val addition = if (lastPart.isEmpty()) "0." else "."
            append(addition)
        }
    }

    private fun appendOperator(operator: String) {
        val expr = _state.value.expression.trim()
        if (expr.isBlank()) return

        val last = expr.last()
        if (last in listOf('+', '-', '*', '/')) {
            val updated = expr.dropLast(1) + operator
            _state.value = _state.value.copy(
                expression = updated,
                result = engine.evaluate(updated)
            )
        } else {
            _state.value = _state.value.copy(
                expression = expr + operator
            )
        }
    }

    private fun delete() {
        val expr = _state.value.expression
        if (expr.isNotEmpty()) {
            val updated = expr.dropLast(1)
            _state.value = _state.value.copy(
                expression = updated,
                result = if (updated.isBlank()) "0" else engine.evaluate(updated)
            )
        }
    }

    private fun calculate() {
        val expr = _state.value.expression
        if (expr.isBlank()) return

        val result = engine.evaluate(expr)
        _state.value = _state.value.copy(
            expression = if (result == "Error") expr else result,
            result = result
        )
    }
}