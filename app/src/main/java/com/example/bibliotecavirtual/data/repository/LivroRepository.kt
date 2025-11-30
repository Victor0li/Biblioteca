package com.example.bibliotecavirtual.data.repository

import com.example.bibliotecavirtual.data.Livro
import com.example.bibliotecavirtual.data.db.LivroDao
import kotlinx.coroutines.flow.Flow

class LivroRepository(private val livroDao: LivroDao) {

    val allLivros: Flow<List<Livro>> = livroDao.getAll()
    val favoritos: Flow<List<Livro>> = livroDao.getFavorites()

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
}