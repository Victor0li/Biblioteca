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
import com.example.bibliotecavirtual.data.network.RetrofitClient // IMPORTADO: Cliente de Rede
import com.example.bibliotecavirtual.ui.screens.*
import com.example.bibliotecavirtual.ui.theme.BibliotecaVirtualTheme
import com.example.bibliotecavirtual.ui.viewmodel.LivroViewModel
import com.example.bibliotecavirtual.ui.viewmodel.LivroViewModelFactory

class MainActivity : ComponentActivity() {

    // 1. Inicializa o serviço de rede.
    private val booksService = RetrofitClient.service

    // 2. Cria o ViewModel, injetando o Repository, que agora depende do DAO e do Service.
    private val livroViewModel: LivroViewModel by viewModels {
        LivroViewModelFactory(
            LivroRepository(
                AppDatabase.getDatabase(application).livroDao(),
                booksService // Injetando a dependência de rede
            )
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

        // 1. Tela principal
        composable("listagem") {
            LivroListScreen(navController, viewModel)
        }

        // 2. Tela de Adicionar/Editar Livro
        composable(
            route = "add_edit/{livroId}",
            arguments = listOf(navArgument("livroId") { type = NavType.IntType; defaultValue = 0 })
        ) { backStackEntry ->
            val livroId = backStackEntry.arguments?.getInt("livroId") ?: 0
            AddEditScreen(navController, livroId, viewModel)
        }

        // 3. Tela de Detalhes do Livro
        composable(
            route = "detail/{livroId}",
            arguments = listOf(navArgument("livroId") { type = NavType.IntType })
        ) { backStackEntry ->
            val livroId = backStackEntry.arguments?.getInt("livroId") ?: 0
            LivroDetailScreen(navController, livroId, viewModel)
        }

        // 4. Tela de Favoritos
        composable("favoritos") {
            FavoritosScreen(navController, viewModel)
        }

        // 5. NOVA TELA: Pesquisa por ISBN (a ser criada)
        composable("search") {
            // Esta é a nova tela que criaremos no próximo passo.
            SearchScreen(navController, viewModel)
        }
    }
}