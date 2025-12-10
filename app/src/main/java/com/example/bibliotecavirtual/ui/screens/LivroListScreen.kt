package com.example.bibliotecavirtual.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bibliotecavirtual.data.Livro
import com.example.bibliotecavirtual.ui.viewmodel.LivroViewModel
import coil.compose.AsyncImage // Import Coil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivroListScreen(navController: NavController, viewModel: LivroViewModel) {
    val livros by viewModel.allLivros.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minha Biblioteca", fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {

                // FAB NOVO: Pesquisar por ISBN
                FloatingActionButton(
                    onClick = { navController.navigate("search") },
                    modifier = Modifier.padding(bottom = 8.dp),
                    containerColor = MaterialTheme.colorScheme.tertiary,
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Pesquisar Livro por ISBN")
                }

                // FAB existente: Favoritos
                FloatingActionButton(
                    onClick = { navController.navigate("favoritos") },
                    modifier = Modifier.padding(bottom = 8.dp),
                    containerColor = MaterialTheme.colorScheme.secondary,
                ) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Favoritos")
                }

                // FAB existente: Adicionar Manualmente (mantido caso queira reintroduzir ou para compatibilidade com a rota)
                FloatingActionButton(
                    onClick = { navController.navigate("add_edit/0") },
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Adicionar Livro")
                }
            }
        }
    ) { paddingValues ->
        if (livros.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Adicione seu primeiro livro.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) {
                items(livros, key = { it.id }) { livro ->
                    LivroCard(
                        livro = livro,
                        onClick = { navController.navigate("detail/${livro.id}") }
                    )
                }
            }
        }
    }
}

@Composable
fun LivroCard(livro: Livro, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- IMAGEM DA CAPA ---
            livro.imageUrl?.let { url ->
                AsyncImage(
                    model = url.replace("http://", "https://"),
                    contentDescription = "Capa do Livro ${livro.titulo}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(height = 90.dp, width = 60.dp) // Tamanho de miniatura
                )
            } ?: run {
                Icon(
                    Icons.Default.MenuBook,
                    contentDescription = "Sem capa",
                    modifier = Modifier.size(60.dp).align(Alignment.CenterVertically),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
            // --- FIM DA IMAGEM ---

            Column { // Coluna para o texto
                Text(
                    livro.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    livro.autor,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssistChip(
                        onClick = {},
                        label = { Text(if (livro.isLido) "LIDO" else "PARA LER") },
                        leadingIcon = { Icon(if (livro.isLido) Icons.Filled.Check else Icons.Filled.MenuBook, contentDescription = null) }
                    )
                    if (livro.isFavorito) {
                        AssistChip(
                            onClick = {},
                            label = { Text("FAVORITO") },
                            leadingIcon = { Icon(Icons.Filled.Favorite, contentDescription = null) }
                        )
                    }
                }
            }
        }
    }
}