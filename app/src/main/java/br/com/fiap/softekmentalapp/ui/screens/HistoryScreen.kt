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
import androidx.navigation.NavHostController
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import br.com.fiap.softekmentalapp.model.CheckinResponse
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavHostController,
    checkinRepository: CheckinRepository,
    token: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var checkins by remember { mutableStateOf<List<CheckinResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val dayFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    LaunchedEffect(token) {
        isLoading = true
        try {
            checkins = checkinRepository.getCheckins(token)
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = e.message ?: "Erro ao carregar histórico"
            checkins = emptyList()
            scope.launch { snackbarHostState.showSnackbar(errorMessage ?: "Erro") }
        } finally {
            isLoading = false
        }
    }

    val filteredCheckins = remember(selectedDate, checkins) {
        selectedDate?.let { date ->
            checkins.filter { dayFormat.format(Date(it.timestamp)) == dayFormat.format(date) }
        } ?: checkins
    }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(year, month, day, 0, 0, 0)
                selectedDate = calendar.time
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Histórico") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2))))
                .padding(padding)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Box
            }

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
                        Button(onClick = { showDatePicker = true }) { Text("Filtrar Data") }
                        OutlinedButton(onClick = { selectedDate = null }) { Text("Limpar Filtro") }
                        OutlinedButton(
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                            onClick = {
                                checkins = emptyList()
                                scope.launch {
                                    snackbarHostState.showSnackbar("Histórico limpo localmente")
                                }
                            }
                        ) { Text("Limpar Histórico") }
                    }
                }

                item { Text("Check-ins Emocionais", style = MaterialTheme.typography.headlineSmall) }

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
                                checkin.note?.let {
                                    Text("Observação: $it", style = MaterialTheme.typography.bodyMedium)
                                }
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
