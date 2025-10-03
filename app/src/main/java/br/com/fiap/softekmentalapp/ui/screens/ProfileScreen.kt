package br.com.fiap.softekmentalapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.softekmentalapp.model.UpdateUserRequest
import br.com.fiap.softekmentalapp.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    token: String,
    onLogout: () -> Unit
) {
    val user = viewModel.user
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    LaunchedEffect(Unit) {
        viewModel.loadProfile(token)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Text(text = error, color = Color.Red)
        } else {
            user?.let {
                TextField(
                    value = it.name,
                    onValueChange = { newName ->
                        viewModel.user = it.copy(name = newName)
                    },
                    label = { Text("Nome") }
                )
                TextField(
                    value = it.email,
                    onValueChange = { newEmail ->
                        viewModel.user = it.copy(email = newEmail)
                    },
                    label = { Text("Email") }
                )

                Button(
                    onClick = {
                        viewModel.updateProfile(
                            userId = it.id,
                            token = token,
                            request = UpdateUserRequest(it.name, it.email)
                        )
                    }
                ) {
                    Text("Salvar Alterações")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Sair")
                }
            }
        }
    }
}

