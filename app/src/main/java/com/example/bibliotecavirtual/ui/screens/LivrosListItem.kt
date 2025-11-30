package com.example.bibliotecavirtual.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bibliotecavirtual.data.Livro
import com.example.bibliotecavirtual.ui.viewmodel.LivroViewModel

@Composable
fun LivroListItem(
    livro: Livro,
    viewModel: LivroViewModel,
    onItemClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onItemClick(livro.id) },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = livro.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = livro.autor,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // Ações (Favorito e Remover)
            Row {
                // Botão Favorito
                IconButton(onClick = { viewModel.toggleFavorito(livro) }) {
                    Icon(
                        imageVector = if (livro.isFavorito) Icons.Filled.Star else Icons.Filled.Star,
                        contentDescription = "Favoritar",
                        tint = if (livro.isFavorito) Color(0xFFFFA726) else Color.Gray
                    )
                }

                // Botão Remover (Apenas ícone, sem confirmação extra)
                IconButton(onClick = { viewModel.deletar(livro) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Remover",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}