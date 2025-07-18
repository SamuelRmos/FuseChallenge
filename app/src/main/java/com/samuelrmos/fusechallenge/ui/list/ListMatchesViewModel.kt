package com.samuelrmos.fusechallenge.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelrmos.fusechallenge.domain.repository.IListMatchesRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListMatchesViewModel(private val listMatchesRepository: IListMatchesRepository) : ViewModel() {

    fun fetchListMatches(page: Int) {
        viewModelScope.launch(IO) {
            listMatchesRepository.fetchRunningMatches(page).collectLatest { result ->

            }
        }
    }
}