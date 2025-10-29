package com.example.wtc_chat.data.repository

import com.example.wtc_chat.data.model.Campaign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CampaignRepository {

    suspend fun sendCampaign(campaign: Campaign): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {

            println("Sending campaign: $campaign")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
