package br.com.fiap.softekmentalapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.fiap.softekmentalapp.viewmodel.InsightsViewModel

@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel,
    token: String
) {
    LaunchedEffect(token) {
        viewModel.loadCheckins(token)
    }

    val checkins by viewModel.checkins.collectAsState()
    val emotionCount = remember(checkins) {
        checkins.groupingBy { it.emotion }.eachCount()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color(0xFF90CAF9))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Seus Insights",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (emotionCount.isNotEmpty()) {
                emotionCount.forEach { (emotion, count) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = emotion.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier
                                    .height(20.dp)
                                    .width((count * 20).coerceAtMost(200).dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(10.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$count",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "Nenhum check-in registrado ainda.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

