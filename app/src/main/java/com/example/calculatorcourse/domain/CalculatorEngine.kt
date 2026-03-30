package com.example.calculatorcourse.domain

class CalculatorEngine {

    fun evaluate(expression: String): String {
        if (expression.isBlank()) return "0"

        return try {
            val normalized = expression.replace(",", ".")
            val result = evaluateSimpleExpression(normalized)
            formatResult(result)
        } catch (e: Exception) {
            "Error"
        }
    }

    private fun evaluateSimpleExpression(expression: String): Double {
        val tokens = tokenize(expression)
        if (tokens.isEmpty()) return 0.0

        val values = mutableListOf<Double>()
        val ops = mutableListOf<Char>()

        fun applyTopOperator() {
            if (values.size < 2 || ops.isEmpty()) return

            val b = values.removeAt(values.lastIndex)
            val a = values.removeAt(values.lastIndex)
            val op = ops.removeAt(ops.lastIndex)

            val result = when (op) {
                '+' -> a + b
                '-' -> a - b
                '*' -> a * b
                '/' -> {
                    if (b == 0.0) throw IllegalArgumentException("Division by zero")
                    a / b
                }
                else -> throw IllegalArgumentException("Unknown operator")
            }

            values.add(result)
        }

        fun precedence(op: Char): Int {
            return when (op) {
                '+', '-' -> 1
                '*', '/' -> 2
                else -> 0
            }
        }

        for (token in tokens) {
            if (token.toDoubleOrNull() != null) {
                values.add(token.toDouble())
            } else {
                val op = token.first()
                while (ops.isNotEmpty() && precedence(ops.last()) >= precedence(op)) {
                    applyTopOperator()
                }
                ops.add(op)
            }
        }

        while (ops.isNotEmpty()) {
            applyTopOperator()
        }

        return values.firstOrNull() ?: 0.0
    }

    private fun tokenize(expression: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()

        expression.forEachIndexed { index, c ->
            when {
                c.isDigit() || c == '.' -> current.append(c)
                c in listOf('+', '-', '*', '/') -> {
                    if (c == '-' && (index == 0 || expression[index - 1] in listOf('+', '-', '*', '/'))) {
                        current.append(c)
                    } else {
                        if (current.isNotEmpty()) {
                            result.add(current.toString())
                            current.clear()
                        }
                        result.add(c.toString())
                    }
                }
            }
        }

        if (current.isNotEmpty()) {
            result.add(current.toString())
        }

        return result
    }

    private fun formatResult(value: Double): String {
        return if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
    }
}