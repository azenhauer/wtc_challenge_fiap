package com.example.wtc_chat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wtc_chat.data.model.Campaign
import com.example.wtc_chat.data.model.Client
import com.example.wtc_chat.data.repository.CampaignRepository
import com.example.wtc_chat.data.repository.CrmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CampaignViewModel : ViewModel() {

    private val campaignRepository = CampaignRepository()
    private val crmRepository = CrmRepository()

    private val _campaignState = MutableStateFlow(Campaign())
    val campaignState: StateFlow<Campaign> = _campaignState.asStateFlow()

    private val _sendState = MutableStateFlow<Result<Unit>?>(null)
    val sendState: StateFlow<Result<Unit>?> = _sendState.asStateFlow()

    private val _segments = MutableStateFlow<List<String>>(emptyList())
    val segments: StateFlow<List<String>> = _segments.asStateFlow()

    private val _clientCount = MutableStateFlow(0)
    val clientCount: StateFlow<Int> = _clientCount.asStateFlow()

    private var allClients: List<Client> = emptyList()

    init {
        crmRepository.getClients()
            .onEach { clients ->
                allClients = clients
                val tags = clients.flatMap { it.tags }.distinct()
                _segments.value = listOf("All") + tags
                updateClientCount()
            }
            .launchIn(viewModelScope)
    }

    fun onTitleChange(title: String) {
        _campaignState.value = _campaignState.value.copy(title = title)
    }

    fun onMessageChange(message: String) {
        _campaignState.value = _campaignState.value.copy(message = message)
    }

    fun onSegmentChange(segment: String) {
        _campaignState.value = _campaignState.value.copy(segment = segment)
        updateClientCount()
    }

    private fun updateClientCount() {
        val selectedSegment = _campaignState.value.segment
        _clientCount.value = when {
            selectedSegment.equals("All", ignoreCase = true) -> allClients.size
            selectedSegment.isNotBlank() -> allClients.count { it.tags.contains(selectedSegment) }
            else -> 0
        }
    }

    fun sendCampaign() {
        viewModelScope.launch {
            _sendState.value = campaignRepository.sendCampaign(_campaignState.value)
        }
    }
}
