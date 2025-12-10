package com.example.bibliotecavirtual.data.repository

import com.example.bibliotecavirtual.data.Livro
import com.example.bibliotecavirtual.data.db.LivroDao
import com.example.bibliotecavirtual.data.network.GoogleBooksService
import com.example.bibliotecavirtual.data.network.Item
import kotlinx.coroutines.flow.Flow

// O Repository agora recebe o DAO (Room) e o Service (Retrofit)
class LivroRepository(
    private val livroDao: LivroDao,
    private val booksService: GoogleBooksService
) {

    // --- FUNÇÕES DE PERSISTÊNCIA (ROOM) ---

    val allLivros: Flow<List<Livro>> = livroDao.getAll()
    val favoritos: Flow<List<Livro>> = livroDao.getFavorites()
    val lidos: Flow<List<Livro>> = livroDao.getLidos()
    val paraLer: Flow<List<Livro>> = livroDao.getParaLer()

    fun getLivroById(id: Int): Flow<Livro?> = livroDao.getLivroById(id)

    suspend fun inserir(livro: Livro) {
        livroDao.inserir(livro)
    }

    suspend fun deletar(livro: Livro) {
        livroDao.deletar(livro)
    }

    suspend fun atualizar(livro: Livro) {
        livroDao.atualizar(livro)
    }

    suspend fun toggleFavorito(livro: Livro) {
        val updatedLivro = livro.copy(isFavorito = !livro.isFavorito)
        livroDao.atualizar(updatedLivro)
    }

    suspend fun toggleLido(livro: Livro) {
        val updatedLivro = livro.copy(isLido = !livro.isLido)
        livroDao.atualizar(updatedLivro)
    }

    // --- FUNÇÕES DE BUSCA EXTERNA (API GOOGLE BOOKS) ---

    /**
     * Mapeia o item da API do Google Books para a entidade Livro local.
     */
    private fun mapApiItemToLivro(item: Item): Livro {
        val volumeInfo = item.volumeInfo

        // Determinar o gênero
        val genre = volumeInfo.categories?.firstOrNull() ?: "Não especificado"

        // Determinar o ano (usando apenas os primeiros 4 dígitos)
        val ano = volumeInfo.anoPublicacao?.substringBefore('-')?.toIntOrNull() ?: 0

        val imageUrl = item.volumeInfo.imageLinks?.thumbnail

        // Retorna o objeto Livro, com ID=0 para que o Room o gere na inserção
        return Livro(
            id = 0,
            titulo = volumeInfo.title ?: "Título Desconhecido",
            autor = volumeInfo.authors?.joinToString(", ") ?: "Autor Desconhecido",
            genre = genre,
            anoPublicacao = ano,
            description = volumeInfo.description ?: "Sem descrição disponível.",
            isFavorito = false, // Status padrão
            isLido = false,       // Status padrão
            imageUrl = imageUrl
        )
    }

    /**
     * Busca um livro na API do Google Books usando o ISBN.
     * @return Livro? Mapeado para a entidade local, ou null se não for encontrado.
     */
    suspend fun searchBookByIsbn(isbn: String): Livro? {
        // A API de Volumes usa o parâmetro 'q' no formato "isbn:SEU_ISBN"
        val query = "isbn:$isbn"

        return try {
            val response = booksService.searchBooks(query)

            // Se a busca retornar itens e o primeiro for válido
            if (response.totalItems > 0 && !response.items.isNullOrEmpty()) {
                mapApiItemToLivro(response.items.first())
            } else {
                null // Livro não encontrado
            }
        } catch (e: Exception) {
            // Logar a exceção ou lidar com falhas de rede
            println("Erro ao buscar livro na API: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}