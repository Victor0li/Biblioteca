package com.example.bibliotecavirtual.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bibliotecavirtual.ui.viewmodel.LivroViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivroListScreen(navController: NavController, viewModel: LivroViewModel) {
    val livros by viewModel.allLivros.observeAsState(initial = emptyList())

    Scaffold(
        topBar = { TopAppBar(title = { Text("Minha Biblioteca") }) },
        floatingActionButton = {
            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                // Botão Favoritos
                FloatingActionButton(
                    onClick = { navController.navigate("favoritos") },
                    modifier = Modifier.padding(bottom = 8.dp),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    content = { Icon(Icons.Filled.Favorite, contentDescription = "Favoritos") }
                )
                // Botão Adicionar
                FloatingActionButton(
                    onClick = { navController.navigate("add_edit/0") },
                    content = { Icon(Icons.Filled.Add, contentDescription = "Adicionar Livro") }
                )
            }
        }
    ) { paddingValues ->
        if (livros.isEmpty()) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Adicione seu primeiro livro.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) {
                items(livros, key = { it.id }) { livro ->
                    LivroListItem(
                        livro = livro,
                        viewModel = viewModel,
                        onItemClick = { livroId ->
                            navController.navigate("detail/$livroId")
                        }
                    )
                }
            }
        }
    }
}