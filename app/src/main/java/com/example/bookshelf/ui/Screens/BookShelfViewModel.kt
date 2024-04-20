package com.example.bookshelf.ui.Screens


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.data.BookRepository
import com.example.bookshelf.model.Book
import com.example.bookshelf.BookShelfApplication
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface BookShelfUiState {
    data class Success(val books: List<Book>) : BookShelfUiState
    object Error : BookShelfUiState
    object Loading : BookShelfUiState
}

class BookShelfViewModel (private val bookRepository: BookRepository) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookShelfApplication)
                val bookRepository = application.container.bookRepository
                BookShelfViewModel(bookRepository = bookRepository)
            }
        }
    }


    /** The mutable State that stores the status of the most recent request */
    var bookShelfUiState: BookShelfUiState by mutableStateOf(BookShelfUiState.Loading)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getBooks()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun getBooks() {
        viewModelScope.launch {
            bookShelfUiState = BookShelfUiState.Loading
            bookShelfUiState = try {
                BookShelfUiState.Success(
                    bookRepository.getBooks()
                )
            } catch (e: IOException) {
                BookShelfUiState.Error
            } catch (e: HttpException) {
                BookShelfUiState.Error
            }
        }
    }
}

