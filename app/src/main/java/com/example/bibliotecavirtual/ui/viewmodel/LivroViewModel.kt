package com.example.bibliotecavirtual.ui.viewmodel

import androidx.lifecycle.*
import com.example.bibliotecavirtual.data.Livro
import com.example.bibliotecavirtual.data.repository.LivroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LivroViewModel(private val repository: LivroRepository) : ViewModel() {

    // --- ESTADO DA PESQUISA ---

    // Estado que armazena o Livro encontrado na API (ou null se não encontrado/não pesquisado)
    private val _searchedLivro = MutableStateFlow<Livro?>(null)
    val searchedLivro: StateFlow<Livro?> = _searchedLivro.asStateFlow()

    // Estado que armazena se a busca está em andamento
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // Estado para mensagens de erro
    private val _searchErrorMessage = MutableStateFlow<String?>(null)
    val searchErrorMessage: StateFlow<String?> = _searchErrorMessage.asStateFlow()

    // --- FUNÇÕES DE PESQUISA ---

    /**
     * Busca um livro pelo ISBN usando a API e atualiza o estado.
     */
    fun searchBook(isbn: String) = viewModelScope.launch {
        // Limpa estados anteriores
        _searchedLivro.value = null
        _searchErrorMessage.value = null
        _isSearching.value = true

        try {
            val livroEncontrado = repository.searchBookByIsbn(isbn)

            if (livroEncontrado != null) {
                _searchedLivro.value = livroEncontrado
            } else {
                _searchErrorMessage.value = "Livro não encontrado com o ISBN: $isbn"
            }
        } catch (e: Exception) {
            _searchErrorMessage.value = "Erro de rede. Verifique sua conexão."
        } finally {
            _isSearching.value = false
        }
    }

    /**
     * Limpa o estado da pesquisa após a adição ou navegação.
     */
    fun clearSearchState() {
        _searchedLivro.value = null
        _searchErrorMessage.value = null
        _isSearching.value = false
    }

    // --- FUNÇÕES DE PERSISTÊNCIA E LEITURA EXISTENTES (SEM MUDANÇAS) ---

    // Leitura: Convertendo Flow (do Repository) para LiveData (para UI)
    val allLivros: LiveData<List<Livro>> = repository.allLivros.asLiveData()
    val favoritos: LiveData<List<Livro>> = repository.favoritos.asLiveData()
    val lidos: LiveData<List<Livro>> = repository.lidos.asLiveData()
    val paraLer: LiveData<List<Livro>> = repository.paraLer.asLiveData()

    fun getLivroById(id: Int): LiveData<Livro?> {
        if (id == 0) return MutableLiveData(null)
        return repository.getLivroById(id).asLiveData()
    }

    // Escrita: Executando operações de suspensão no viewModelScope
    fun inserir(livro: Livro) = viewModelScope.launch {
        repository.inserir(livro)
        // Se o livro for inserido com sucesso, limpar o estado de pesquisa (opcional, mas útil)
        clearSearchState()
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

    fun toggleLido(livro: Livro) = viewModelScope.launch {
        repository.toggleLido(livro)
    }
}