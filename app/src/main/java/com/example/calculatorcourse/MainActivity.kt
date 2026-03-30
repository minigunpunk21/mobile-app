package com.example.calculatorcourse

import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calculatorcourse.domain.CalculatorAction
import com.example.calculatorcourse.ui.CalculatorViewModel
import com.example.calculatorcourse.ui.ShakeDetector

class MainActivity : ComponentActivity() {

    private val viewModel: CalculatorViewModel by viewModels()
    private var sensorManager: SensorManager? = null
    private var shakeDetector: ShakeDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        shakeDetector = ShakeDetector {
            runOnUiThread { viewModel.clear() }
        }

        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                CalculatorApp(viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer != null && shakeDetector != null) {
            sensorManager?.registerListener(
                shakeDetector,
                accelerometer,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onPause() {
        super.onPause()
        shakeDetector?.let { sensorManager?.unregisterListener(it) }
    }
}

private data class CalculatorButton(
    val title: String,
    val action: CalculatorAction,
    val color: Color = Color(0xFF2A2A2A)
)

@Composable
fun CalculatorApp(viewModel: CalculatorViewModel) {
    val state by viewModel.state.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val buttons = listOf(
        CalculatorButton("C", CalculatorAction.Clear, Color(0xFF8B0000)),
        CalculatorButton("⌫", CalculatorAction.Delete, Color(0xFF444444)),
        CalculatorButton("/", CalculatorAction.Divide, Color(0xFFFF9800)),
        CalculatorButton("*", CalculatorAction.Multiply, Color(0xFFFF9800)),
        CalculatorButton("7", CalculatorAction.Number(7)),
        CalculatorButton("8", CalculatorAction.Number(8)),
        CalculatorButton("9", CalculatorAction.Number(9)),
        CalculatorButton("-", CalculatorAction.Subtract, Color(0xFFFF9800)),
        CalculatorButton("4", CalculatorAction.Number(4)),
        CalculatorButton("5", CalculatorAction.Number(5)),
        CalculatorButton("6", CalculatorAction.Number(6)),
        CalculatorButton("+", CalculatorAction.Add, Color(0xFFFF9800)),
        CalculatorButton("1", CalculatorAction.Number(1)),
        CalculatorButton("2", CalculatorAction.Number(2)),
        CalculatorButton("3", CalculatorAction.Number(3)),
        CalculatorButton("=", CalculatorAction.Calculate, Color(0xFF4CAF50)),
        CalculatorButton("0", CalculatorAction.Number(0)),
        CalculatorButton(".", CalculatorAction.Decimal)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) { padding ->
        if (isLandscape) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(padding)
                    .padding(16.dp)
            ) {
                val leftWidth = maxWidth * 0.4f
                val rightWidth = maxWidth * 0.6f

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .width(leftWidth)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "Shake phone to clear",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = state.expression.ifBlank { "0" },
                            color = Color.LightGray,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        )

                        Text(
                            text = state.result,
                            color = Color.White,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        contentPadding = PaddingValues(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .width(rightWidth)
                            .fillMaxHeight()
                    ) {
                        items(buttons) { button ->
                            CalculatorButtonItem(
                                title = button.title,
                                color = button.color,
                                compact = true
                            ) {
                                viewModel.onAction(button.action)
                            }
                        }

                        item { Box(modifier = Modifier.size(0.dp)) }
                        item { Box(modifier = Modifier.size(0.dp)) }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Shake phone to clear",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = state.expression.ifBlank { "0" },
                        color = Color.LightGray,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 24.dp)
                    )

                    Text(
                        text = state.result,
                        color = Color.White,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(buttons) { button ->
                        CalculatorButtonItem(
                            title = button.title,
                            color = button.color,
                            compact = false
                        ) {
                            viewModel.onAction(button.action)
                        }
                    }

                    item { Box(modifier = Modifier.size(0.dp)) }
                    item { Box(modifier = Modifier.size(0.dp)) }
                }
            }
        }
    }
}

@Composable
fun CalculatorButtonItem(
    title: String,
    color: Color,
    compact: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(if (compact) 1.4f else 1f),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(
            text = title,
            style = if (compact) {
                MaterialTheme.typography.headlineSmall
            } else {
                MaterialTheme.typography.headlineMedium
            },
            color = Color.White
        )
    }
}