package com.example.bookshelf.data

import com.example.bookshelf.model.Book
import com.example.bookshelf.network.BookShelfApiService


interface BookRepository {
    suspend fun getBooks(): List<Book>

}

class DefaultBookRepository(
    private val bookShelfApiService: BookShelfApiService
) : BookRepository {
    override suspend fun getBooks(): List<Book> = bookShelfApiService.getBooks()
}