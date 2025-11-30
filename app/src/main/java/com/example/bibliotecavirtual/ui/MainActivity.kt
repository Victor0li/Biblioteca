package com.example.bibliotecavirtual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bibliotecavirtual.data.db.AppDatabase
import com.example.bibliotecavirtual.data.repository.LivroRepository
import com.example.bibliotecavirtual.ui.screens.*
import com.example.bibliotecavirtual.ui.theme.BibliotecaVirtualTheme
import com.example.bibliotecavirtual.ui.viewmodel.LivroViewModel
import com.example.bibliotecavirtual.ui.viewmodel.LivroViewModelFactory

class MainActivity : ComponentActivity() {

    private val livroViewModel: LivroViewModel by viewModels {
        LivroViewModelFactory(
            LivroRepository(AppDatabase.getDatabase(application).livroDao())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BibliotecaVirtualTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(livroViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: LivroViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "listagem") {
        composable("listagem") {
            LivroListScreen(navController, viewModel)
        }

        composable(
            route = "add_edit/{livroId}",
            arguments = listOf(navArgument("livroId") { type = NavType.IntType; defaultValue = 0 })
        ) { backStackEntry ->
            val livroId = backStackEntry.arguments?.getInt("livroId") ?: 0
            AddEditScreen(navController, livroId, viewModel)
        }

        composable(
            route = "detail/{livroId}",
            arguments = listOf(navArgument("livroId") { type = NavType.IntType })
        ) { backStackEntry ->
            val livroId = backStackEntry.arguments?.getInt("livroId") ?: 0
            LivroDetailScreen(navController, livroId, viewModel)
        }

        composable("favoritos") {
            FavoritosScreen(navController, viewModel)
        }
    }
}