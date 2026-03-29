package com.example.calculatorcourse.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculatorcourse.domain.CalculatorAction
import com.example.calculatorcourse.domain.CalculatorOperation
import com.example.calculatorcourse.ui.components.CalculatorButton
import com.example.calculatorcourse.ui.components.CalculatorButtonModel

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel = viewModel()) {
    val state = viewModel.state

    val buttons = listOf(
        CalculatorButtonModel.Text("AC") { viewModel.onAction(CalculatorAction.Clear) },
        CalculatorButtonModel.Text("+/-") { viewModel.onAction(CalculatorAction.ToggleSign) },
        CalculatorButtonModel.Text("%") { viewModel.onAction(CalculatorAction.Percent) },
        CalculatorButtonModel.Text("÷", true) { viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Divide)) },

        CalculatorButtonModel.Text("7") { viewModel.onAction(CalculatorAction.Number(7)) },
        CalculatorButtonModel.Text("8") { viewModel.onAction(CalculatorAction.Number(8)) },
        CalculatorButtonModel.Text("9") { viewModel.onAction(CalculatorAction.Number(9)) },
        CalculatorButtonModel.Text("×", true) { viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Multiply)) },

        CalculatorButtonModel.Text("4") { viewModel.onAction(CalculatorAction.Number(4)) },
        CalculatorButtonModel.Text("5") { viewModel.onAction(CalculatorAction.Number(5)) },
        CalculatorButtonModel.Text("6") { viewModel.onAction(CalculatorAction.Number(6)) },
        CalculatorButtonModel.Text("-") { viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Subtract)) },

        CalculatorButtonModel.Text("1") { viewModel.onAction(CalculatorAction.Number(1)) },
        CalculatorButtonModel.Text("2") { viewModel.onAction(CalculatorAction.Number(2)) },
        CalculatorButtonModel.Text("3") { viewModel.onAction(CalculatorAction.Number(3)) },
        CalculatorButtonModel.Text("+") { viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Add)) },

        CalculatorButtonModel.Icon(Icons.AutoMirrored.Outlined.Backspace) { viewModel.onAction(CalculatorAction.Delete) },
        CalculatorButtonModel.Text("0") { viewModel.onAction(CalculatorAction.Number(0)) },
        CalculatorButtonModel.Text(".") { viewModel.onAction(CalculatorAction.Decimal) },
        CalculatorButtonModel.Icon(Icons.Outlined.Calculate, true) { viewModel.onAction(CalculatorAction.Calculate) }
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isCompact = maxWidth < 600.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isCompact) 16.dp else 32.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(if (isCompact) 0.33f else 0.4f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = state.expression,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = state.display,
                    style = if (isCompact) MaterialTheme.typography.displayLarge else MaterialTheme.typography.displayMedium,
                    color = if (state.hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(buttons) { button ->
                    CalculatorButton(
                        model = button,
                        modifier = Modifier.size(if (isCompact) 72.dp else 88.dp)
                    )
                }
            }
        }
    }
}
