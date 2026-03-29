package com.example.calculatorcourse.domain

import java.math.BigDecimal
import java.math.RoundingMode

class CalculatorEngine {
    private var currentInput = "0"
    private var storedValue: BigDecimal? = null
    private var pendingOperation: CalculatorOperation? = null
    private var expressionText = ""
    private var resetInputOnNextDigit = false
    private var errorState = false

    fun state(): CalculatorState = CalculatorState(
        display = currentInput,
        expression = expressionText,
        hasError = errorState
    )

    fun onAction(action: CalculatorAction): CalculatorState {
        if (errorState && action !is CalculatorAction.Clear) {
            clearAll()
        }

        when (action) {
            is CalculatorAction.Number -> appendNumber(action.value)
            CalculatorAction.Decimal -> appendDecimal()
            CalculatorAction.Clear -> clearAll()
            CalculatorAction.Delete -> deleteLast()
            CalculatorAction.Calculate -> calculate()
            CalculatorAction.ToggleSign -> toggleSign()
            CalculatorAction.Percent -> percent()
            is CalculatorAction.Operation -> chooseOperation(action.value)
        }
        return state()
    }

    private fun appendNumber(number: Int) {
        if (resetInputOnNextDigit) {
            currentInput = number.toString()
            resetInputOnNextDigit = false
            return
        }

        currentInput = if (currentInput == "0") number.toString() else currentInput + number
    }

    private fun appendDecimal() {
        if (resetInputOnNextDigit) {
            currentInput = "0."
            resetInputOnNextDigit = false
            return
        }
        if (!currentInput.contains('.')) {
            currentInput += "."
        }
    }

    private fun clearAll() {
        currentInput = "0"
        storedValue = null
        pendingOperation = null
        expressionText = ""
        resetInputOnNextDigit = false
        errorState = false
    }

    private fun deleteLast() {
        if (resetInputOnNextDigit) return

        currentInput = when {
            currentInput.length <= 1 -> "0"
            currentInput.startsWith("-") && currentInput.length == 2 -> "0"
            else -> currentInput.dropLast(1)
        }
    }

    private fun toggleSign() {
        if (currentInput == "0") return
        currentInput = if (currentInput.startsWith("-")) {
            currentInput.removePrefix("-")
        } else {
            "-$currentInput"
        }
    }

    private fun percent() {
        val value = currentInput.toBigDecimalOrNull() ?: run {
            setError()
            return
        }
        currentInput = format(value.divide(BigDecimal(100), 12, RoundingMode.HALF_UP))
    }

    private fun chooseOperation(operation: CalculatorOperation) {
        val currentValue = currentInput.toBigDecimalOrNull() ?: run {
            setError()
            return
        }

        if (pendingOperation != null && !resetInputOnNextDigit) {
            val result = performOperation(storedValue, currentValue, pendingOperation)
            if (result == null) {
                setError()
                return
            }
            storedValue = result
            currentInput = format(result)
        } else {
            storedValue = currentValue
        }

        pendingOperation = operation
        expressionText = "${format(storedValue)} ${operation.symbol}"
        resetInputOnNextDigit = true
    }

    private fun calculate() {
        val left = storedValue ?: return
        val operation = pendingOperation ?: return
        val right = currentInput.toBigDecimalOrNull() ?: run {
            setError()
            return
        }

        val result = performOperation(left, right, operation) ?: run {
            setError()
            return
        }

        expressionText = "${format(left)} ${operation.symbol} ${format(right)} ="
        currentInput = format(result)
        storedValue = null
        pendingOperation = null
        resetInputOnNextDigit = true
    }

    private fun performOperation(
        left: BigDecimal?,
        right: BigDecimal,
        operation: CalculatorOperation?
    ): BigDecimal? {
        if (left == null || operation == null) return null
        return when (operation) {
            CalculatorOperation.Add -> left + right
            CalculatorOperation.Subtract -> left - right
            CalculatorOperation.Multiply -> left.multiply(right)
            CalculatorOperation.Divide -> {
                if (right.compareTo(BigDecimal.ZERO) == 0) null
                else left.divide(right, 12, RoundingMode.HALF_UP)
            }
        }
    }

    private fun format(value: BigDecimal?): String {
        if (value == null) return "0"
        return value.stripTrailingZeros().toPlainString()
    }

    private fun setError() {
        currentInput = "Error"
        expressionText = "Invalid operation"
        storedValue = null
        pendingOperation = null
        resetInputOnNextDigit = true
        errorState = true
    }
}
