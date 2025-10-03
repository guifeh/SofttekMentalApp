package br.com.fiap.softekmentalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 400.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Perfil",
                fontSize = 32.sp,
                fontWeight = FontWeight.Light,
                color = Color(0xFF1A1A1A),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            if (isLoading) {
                CircularProgressIndicator(
                    color = Color(0xFF1A1A1A),
                    strokeWidth = 2.dp
                )
            } else if (error != null) {
                Text(
                    text = error,
                    color = Color(0xFFE53935),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else {
                user?.let {
                    // Campo Nome
                    OutlinedTextField(
                        value = it.name,
                        onValueChange = { newName ->
                            viewModel.user = it.copy(name = newName)
                        },
                        label = {
                            Text(
                                "Nome",
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1A1A1A),
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedLabelColor = Color(0xFF1A1A1A),
                            unfocusedLabelColor = Color(0xFF666666),
                            cursorColor = Color(0xFF1A1A1A)
                        ),
                        singleLine = true
                    )

                    // Campo Email
                    OutlinedTextField(
                        value = it.email,
                        onValueChange = { newEmail ->
                            viewModel.user = it.copy(email = newEmail)
                        },
                        label = {
                            Text(
                                "Email",
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1A1A1A),
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedLabelColor = Color(0xFF1A1A1A),
                            unfocusedLabelColor = Color(0xFF666666),
                            cursorColor = Color(0xFF1A1A1A)
                        ),
                        singleLine = true
                    )

                    // Botão Salvar
                    Button(
                        onClick = {
                            viewModel.updateProfile(
                                userId = it.id,
                                token = token,
                                request = UpdateUserRequest(it.name, it.email)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A1A1A)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        Text(
                            "Salvar Alterações",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botão Sair
                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFE53935)
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.dp,
                            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE53935))
                        )
                    ) {
                        Text(
                            "Sair",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
