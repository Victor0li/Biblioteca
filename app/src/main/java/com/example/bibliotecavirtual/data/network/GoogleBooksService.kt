package com.example.bibliotecavirtual.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName

// 1. Data classes para mapear a resposta simplificada da API do Google Books
// A resposta é complexa, vamos mapear apenas o necessário (volumes -> items -> volumeInfo)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    @SerializedName("publishedDate")
    val anoPublicacao: String?, // Retorna como String "YYYY-MM-DD" ou apenas "YYYY"
    val categories: List<String>?, // Pode ser usado como 'genre'
    val imageLinks: ImageLinks?
)

data class Item(
    val volumeInfo: VolumeInfo
)

data class BooksResponse(
    val totalItems: Int,
    val items: List<Item>?
)

data class ImageLinks(
    val thumbnail: String?
)

// 2. Interface Retrofit
interface GoogleBooksService {
    @GET("volumes")
    suspend fun searchBooks(
        // Query para buscar por ISBN: "isbn:SEU_ISBN"
        @Query("q") query: String
    ): BooksResponse
}