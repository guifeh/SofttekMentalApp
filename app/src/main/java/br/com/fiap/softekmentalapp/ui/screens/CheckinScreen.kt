package br.com.fiap.softekmentalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.softekmentalapp.navigation.AppScreen
import br.com.fiap.softekmentalapp.viewmodel.CheckinViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CheckinScreen(
    navController: NavController,
    coroutineScope: CoroutineScope,
    viewModel: CheckinViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Como você está se sentindo hoje?",
            fontSize = 26.sp,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium
        )

        EmotionButton(
            text = "Feliz",
            color = Color(0xFF81C784),
            icon = Icons.Default.Mood
        ) {
            coroutineScope.launch {
                viewModel.addCheckin("happy")
                navController.navigate(AppScreen.Feedback.createRoute("happy"))
            }
        }

        EmotionButton(
            text = "Triste",
            color = Color(0xFFE57373),
            icon = Icons.Default.MoodBad
        ) {
            coroutineScope.launch {
                viewModel.addCheckin("sad")
                navController.navigate(AppScreen.Feedback.createRoute("sad"))
            }
        }

        EmotionButton(
            text = "Ansioso",
            color = Color(0xFFFFB74D),
            icon = Icons.Default.SentimentVeryDissatisfied
        ) {
            coroutineScope.launch {
                viewModel.addCheckin("anxious")
                navController.navigate(AppScreen.Feedback.createRoute("anxious"))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = { navController.navigate(AppScreen.History.route) },
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Icon(Icons.Default.History, contentDescription = "Histórico")
            Spacer(Modifier.width(8.dp))
            Text("Ver Histórico")
        }

        OutlinedButton(
            onClick = { navController.navigate(AppScreen.Insights.route) },
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Icon(Icons.Default.Insights, contentDescription = "Insights")
            Spacer(Modifier.width(8.dp))
            Text("Ver Insights")
        }
    }
}

@Composable
fun EmotionButton(
    text: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(56.dp)
    ) {
        Icon(icon, contentDescription = text)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 18.sp)
    }
}
