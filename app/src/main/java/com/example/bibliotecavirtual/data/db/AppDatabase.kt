package com.example.bibliotecavirtual.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bibliotecavirtual.data.Livro

// VERSÃO CORRIGIDA PARA 3
@Database(entities = [Livro::class], version = 3, exportSchema = false) // <--- MUDAR PARA 3
abstract class AppDatabase : RoomDatabase() {

    abstract fun livroDao(): LivroDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "livro_database"
                )
                    // Esta linha garante que o banco de dados será apagado e recriado na mudança de versão.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}