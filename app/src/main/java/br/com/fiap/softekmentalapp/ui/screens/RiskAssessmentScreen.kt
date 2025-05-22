package br.com.fiap.softekmentalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.softekmentalapp.model.AssessmentResult
import br.com.fiap.softekmentalapp.repository.AssessmentRepository
import kotlinx.coroutines.launch

data class Question(val id: Int, val text: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiskAssessmentScreen(navController: NavController) {
    // Lista de perguntas
    val questions = listOf(
        Question(1, "Com que frequência você se sente sobrecarregado(a) com suas tarefas?"),
        Question(2, "Você sente que tem apoio suficiente no trabalho?"),
        Question(3, "Você tem dificuldades para dormir por conta de preocupações com o trabalho?")
    )

    // Estados do componente
    val responses = remember { mutableStateMapOf<Int, Int>() }
    var showResult by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Diálogo de resultado
    if (showResult) {
        AlertDialog(
            onDismissRequest = { showResult = false },
            confirmButton = {
                TextButton(onClick = { showResult = false }) {
                    Text("OK")
                }
            },
            title = { Text("Resultado da Avaliação") },
            text = { Text(resultText) }
        )
    }

    // Estrutura da tela
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Avaliação Psicossocial") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Lista de perguntas
            questions.forEach { question ->
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = question.text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // Opções de resposta (1-5)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                (1..5).forEach { value ->
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        RadioButton(
                                            selected = responses[question.id] == value,
                                            onClick = { responses[question.id] = value }
                                        )
                                        Text(text = "$value")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Botão de envio
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (responses.size == questions.size) {
                            isLoading = true
                            scope.launch {
                                try {
                                    val totalScore = responses.values.sum()
                                    val average = totalScore / questions.size.toDouble()
                                    val classification = when {
                                        average <= 2 -> "Risco Baixo. Continue cuidando da sua saúde mental!"
                                        average <= 3.5 -> "Risco Moderado. Fique atento e busque apoio se necessário."
                                        else -> "Risco Alto. Recomendamos procurar ajuda especializada."
                                    }

                                    AssessmentRepository.addResult(
                                        AssessmentResult(
                                            timestamp = System.currentTimeMillis(),
                                            responses = responses.toMap(),
                                            score = average,
                                            classification = classification
                                        )
                                    )

                                    resultText = classification
                                } catch (e: Exception) {
                                    resultText = "Erro ao salvar: ${e.localizedMessage}"
                                } finally {
                                    isLoading = false
                                    showResult = true
                                }
                            }
                        } else {
                            resultText = "Por favor, responda todas as perguntas."
                            showResult = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 8.dp)
                            )
                            Text("Salvando...")
                        }
                    } else {
                        Text("Enviar Avaliação")
                    }
                }
            }
        }
    }
}