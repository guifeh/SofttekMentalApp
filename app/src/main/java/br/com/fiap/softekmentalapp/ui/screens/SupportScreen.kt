package br.com.fiap.softekmentalapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.softekmentalapp.repository.SupportContentRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var contents by remember { mutableStateOf<List<SupportContent>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            contents = SupportContentRepository.getAllContents()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Apoio e Conteúdo") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(contents) { content ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(content.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(content.description)

                        if (content.url != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(content.url))
                                context.startActivity(intent)
                            }) {
                                Text("Acessar")
                            }
                        }
                    }
                }
            }
        }
    }
}