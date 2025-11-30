package com.example.bibliotecavirtual.ui.viewmodel

import androidx.lifecycle.*
import com.example.bibliotecavirtual.data.Livro
import com.example.bibliotecavirtual.data.repository.LivroRepository
import kotlinx.coroutines.launch

class LivroViewModel(private val repository: LivroRepository) : ViewModel() {

    val allLivros: LiveData<List<Livro>> = repository.allLivros.asLiveData()
    val favoritos: LiveData<List<Livro>> = repository.favoritos.asLiveData()

    fun getLivroById(id: Int): LiveData<Livro?> {
        if (id == 0) return MutableLiveData(null)
        return repository.getLivroById(id).asLiveData()
    }

    fun inserir(livro: Livro) = viewModelScope.launch {
        repository.inserir(livro)
    }

    fun deletar(livro: Livro) = viewModelScope.launch {
        repository.deletar(livro)
    }

    fun atualizar(livro: Livro) = viewModelScope.launch {
        repository.atualizar(livro)
    }

    fun toggleFavorito(livro: Livro) = viewModelScope.launch {
        repository.toggleFavorito(livro)
    }
}