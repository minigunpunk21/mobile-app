package com.example.calculatorcourse.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

sealed interface CalculatorButtonModel {
    val isAccent: Boolean
    val onClick: () -> Unit

    data class Text(
        val value: String,
        override val isAccent: Boolean = false,
        override val onClick: () -> Unit
    ) : CalculatorButtonModel

    data class Icon(
        val imageVector: ImageVector,
        override val isAccent: Boolean = false,
        override val onClick: () -> Unit
    ) : CalculatorButtonModel
}

@Composable
fun CalculatorButton(
    model: CalculatorButtonModel,
    modifier: Modifier = Modifier
) {
    val colors = if (model.isAccent) {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    } else {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Button(
        onClick = model.onClick,
        modifier = modifier,
        colors = colors,
        shape = MaterialTheme.shapes.large
    ) {
        when (model) {
            is CalculatorButtonModel.Text -> {
                Text(
                    text = model.value,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            is CalculatorButtonModel.Icon -> {
                Icon(
                    imageVector = model.imageVector,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(0.42f)
                )
            }
        }
    }
}
