package br.com.fiap.softekmentalapp.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.softekmentalapp.repository.AssessmentRepository
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    val context = LocalContext.current

    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    var checkins by remember { mutableStateOf(CheckinRepository.getAllCheckins()) }
    var assessments by remember { mutableStateOf(AssessmentRepository.getAllResults()) }

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
            TopAppBar(title = { Text("Histórico") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { showDatePicker = true }) {
                    Text("Filtrar por Data")
                }

                Button(onClick = {
                    selectedDate = null
                }) {
                    Text("Limpar Filtro")
                }

                OutlinedButton(onClick = {
                    CheckinRepository.clearAllCheckins()
                    AssessmentRepository.clearAllResults()
                    checkins = CheckinRepository.getAllCheckins()
                    assessments = AssessmentRepository.getAllResults()
                }) {
                    Text("Limpar Histórico")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Check-ins Emocionais", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (filteredCheckins.isEmpty()) {
                Text("Nenhum check-in encontrado.")
            } else {
                LazyColumn {
                    items(filteredCheckins.reversed()) { checkin ->
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text("Emoção: ${checkin.emotion.replaceFirstChar { it.uppercase() }}")
                            Text("Data: ${dateFormat.format(Date(checkin.timestamp))}")
                        }
                        Divider()
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Avaliações Psicossociais", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (filteredAssessments.isEmpty()) {
                Text("Nenhuma avaliação encontrada.")
            } else {
                LazyColumn {
                    items(filteredAssessments.reversed()) { result ->
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text("Data: ${dateFormat.format(Date(result.timestamp))}")
                            Text("Média: %.2f".format(result.score))
                            Text("Classificação: ${result.classification}")
                        }
                        Divider()
                    }
                }
            }
        }
    }
}
