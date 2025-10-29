package com.example.wtc_chat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wtc_chat.data.model.Client
import com.example.wtc_chat.data.repository.CrmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CrmViewModel : ViewModel() {

    private val repository = CrmRepository()
    private val _allClients = MutableStateFlow<List<Client>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients: StateFlow<List<Client>> = _clients.asStateFlow()

    init {
        fetchClients()
        observeSearchQuery()
    }

    private fun fetchClients() {
        viewModelScope.launch {
            repository.getClients()
                .catch { e ->

                }
                .collect { clientList ->
                    _allClients.value = clientList
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            combine(_allClients, _searchQuery) { clients, query ->
                if (query.isBlank()) {
                    clients
                } else {
                    clients.filter { it.name.contains(query, ignoreCase = true) }
                }
            }.collect { filteredClients ->
                _clients.value = filteredClients
            }
        }
    }
}
