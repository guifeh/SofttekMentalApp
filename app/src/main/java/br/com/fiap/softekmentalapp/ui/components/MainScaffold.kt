package br.com.fiap.softekmentalapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.softekmentalapp.navigation.AppScreen
import kotlinx.coroutines.launch

data class DrawerItem(
    val title: String,
    val icon: ImageVector,
    val screen: AppScreen
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavController,
    currentScreen: @Composable () -> Unit,
    isDarkTheme: Boolean,
    onThemeUpdated: () -> Unit,
    token: String? = null
) {
    val drawerItems = listOf(
        DrawerItem("Check-in Emocional", Icons.Filled.EmojiEmotions, AppScreen.Checkin),
        DrawerItem("Histórico", Icons.Filled.History, AppScreen.History),
        DrawerItem("Avaliação de Riscos", Icons.Filled.Assessment, AppScreen.RiskAssessment),
        DrawerItem("Suporte", Icons.Filled.Help, AppScreen.Support),
        DrawerItem("Insights", Icons.Filled.Insights, AppScreen.Insights),
        DrawerItem("Profile", Icons.Filled.Person, AppScreen.Profile),
        DrawerItem(
            if (isDarkTheme) "Tema Claro" else "Tema Escuro",
            if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
            AppScreen.Settings
        )
    )

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.title) },
                        selected = false,
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            if (item.title.contains("Tema")) {
                                onThemeUpdated()
                            } else {
                                if (item.screen in listOf(
                                        AppScreen.Checkin,
                                        AppScreen.RiskAssessment,
                                        AppScreen.History,
                                        AppScreen.Insights,
                                        AppScreen.Profile
                                    )
                                ) {
                                    navController.navigate("${item.screen.route}/$token")
                                } else {
                                    navController.navigate(item.screen.route)
                                }
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Softtek Saúde Mental") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { onThemeUpdated() }) {
                            Icon(
                                if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                contentDescription = "Alternar tema"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                currentScreen()
            }
        }
    }
}