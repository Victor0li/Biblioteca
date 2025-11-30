package com.example.bibliotecavirtual.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bibliotecavirtual.ui.viewmodel.LivroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivroDetailScreen(
    navController: NavController,
    livroId: Int,
    viewModel: LivroViewModel
) {
    val livroState by viewModel.getLivroById(livroId).observeAsState()
    val livro = livroState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(livro?.titulo ?: "Detalhes do Livro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    if (livro != null) {
                        IconButton(onClick = { viewModel.toggleFavorito(livro) }) {
                            Icon(
                                imageVector = if (livro.isFavorito) Icons.Filled.Star else Icons.Filled.Star,
                                contentDescription = "Favoritar",
                                tint = if (livro.isFavorito) Color(0xFFFFA726) else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { navController.navigate("add_edit/$livroId") }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (livro == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Livro não encontrado.")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                // Informações essenciais
                Text("Autor: ${livro.autor}", style = MaterialTheme.typography.titleMedium)
                Text("Gênero: ${livro.genre}", style = MaterialTheme.typography.titleMedium)
                Text("Ano: ${livro.anoPublicacao}", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                // Descrição
                Text("Sinopse:", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(livro.description, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))

                // Botão de Remoção (Ação CRUD essencial)
                Button(
                    onClick = {
                        viewModel.deletar(livro)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Remover Livro", color = Color.White)
                }
            }
        }
    }
}