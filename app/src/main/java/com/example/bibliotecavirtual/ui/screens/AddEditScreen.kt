package com.example.bibliotecavirtual.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bibliotecavirtual.data.Livro
import com.example.bibliotecavirtual.ui.viewmodel.LivroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    navController: NavController,
    livroId: Int,
    viewModel: LivroViewModel
) {
    val isEditing = livroId != 0
    val livroState = if (isEditing) viewModel.getLivroById(livroId).observeAsState() else remember { mutableStateOf<Livro?>(null) }
    val livro = livroState.value

    var titulo by remember { mutableStateOf(livro?.titulo ?: "") }
    var autor by remember { mutableStateOf(livro?.autor ?: "") }
    var ano by remember { mutableStateOf(livro?.anoPublicacao?.toString() ?: "") }
    var genre by remember { mutableStateOf(livro?.genre ?: "") }
    var description by remember { mutableStateOf(livro?.description ?: "") }

    LaunchedEffect(livro) {
        if (isEditing && livro != null) {
            titulo = livro.titulo
            autor = livro.autor
            ano = livro.anoPublicacao.toString()
            genre = livro.genre
            description = livro.description
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar" else "Novo Livro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título*") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = autor, onValueChange = { autor = it }, label = { Text("Autor*") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = genre, onValueChange = { genre = it }, label = { Text("Gênero*") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = ano,
                onValueChange = { if (it.length <= 4) ano = it },
                label = { Text("Ano*") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp),
                singleLine = false
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val anoInt = ano.toIntOrNull()
                    val isValid = titulo.isNotBlank() && autor.isNotBlank() && genre.isNotBlank() && description.isNotBlank() && anoInt != null && anoInt > 0

                    if (isValid) {
                        val novoLivro = Livro(
                            id = if (isEditing) livroId else 0,
                            titulo = titulo,
                            autor = autor,
                            anoPublicacao = anoInt,
                            genre = genre,
                            description = description,
                            isFavorito = livro?.isFavorito ?: false
                        )
                        if (isEditing) {
                            viewModel.atualizar(novoLivro)
                        } else {
                            viewModel.inserir(novoLivro)
                        }
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Atualizar" else "Salvar")
            }
        }
    }
}