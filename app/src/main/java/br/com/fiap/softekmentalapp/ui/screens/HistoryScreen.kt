package br.com.fiap.softekmentalapp.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.softekmentalapp.model.AssessmentResult
import br.com.fiap.softekmentalapp.model.Checkin
import br.com.fiap.softekmentalapp.repository.AssessmentRepository
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var checkins by remember { mutableStateOf<List<Checkin>>(emptyList()) }
    var assessments by remember { mutableStateOf<List<AssessmentResult>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            checkins = CheckinRepository.getAllCheckins()
            assessments = AssessmentRepository.getAllResults()
        }
    }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val dayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val filteredCheckins = remember(selectedDate, checkins) {
        selectedDate?.let { date ->
            checkins.filter {
                dayFormat.format(Date(it.timestamp)) == dayFormat.format(date)
            }
        } ?: checkins
    }

    val filteredAssessments = remember(selectedDate, assessments) {
        selectedDate?.let { date ->
            assessments.filter {
                dayFormat.format(Date(it.timestamp)) == dayFormat.format(date)
            }
        } ?: assessments
    }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(year, month, day)
                selectedDate = calendar.time
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico", style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2))
                    )
                )
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { showDatePicker = true }) {
                            Text("Filtrar Data")
                        }
                        OutlinedButton(onClick = { selectedDate = null }) {
                            Text("Limpar Filtro")
                        }
                        OutlinedButton(
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                            onClick = {
                                scope.launch {
                                    CheckinRepository.clearAllCheckins()
                                    AssessmentRepository.clearAllResults()
                                    checkins = CheckinRepository.getAllCheckins()
                                    assessments = AssessmentRepository.getAllResults()
                                }
                            }
                        ) {
                            Text("Limpar Histórico")
                        }
                    }
                }

                item {
                    Text(
                        "Evolução das Avaliações",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                if (filteredAssessments.isEmpty()) {
                    item { Text("Nenhuma avaliação encontrada.") }
                } else {
                    items(filteredAssessments.reversed()) { result ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Data: ${dateFormat.format(Date(result.timestamp))}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    "Média: ${"%.2f".format(result.score)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    "Classificação: ${result.classification}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        "Check-ins Emocionais",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                if (filteredCheckins.isEmpty()) {
                    item { Text("Nenhum check-in encontrado.") }
                } else {
                    items(filteredCheckins.reversed()) { checkin ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Emoção: ${checkin.emotion.replaceFirstChar { it.uppercase() }}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    "Data: ${dateFormat.format(Date(checkin.timestamp))}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
