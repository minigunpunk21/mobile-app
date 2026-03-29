package com.example.calculatorcourse.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.calculatorcourse.domain.CalculatorAction
import com.example.calculatorcourse.domain.CalculatorEngine
import com.example.calculatorcourse.domain.CalculatorState

class CalculatorViewModel : ViewModel() {
    private val engine = CalculatorEngine()

    var state by mutableStateOf(CalculatorState())
        private set

    init {
        state = engine.state()
    }

    fun onAction(action: CalculatorAction) {
        state = engine.onAction(action)
    }
}
