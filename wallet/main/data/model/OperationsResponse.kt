package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OperationsHistoryResponse(
    @SerialName("success") val isSuccess: Boolean,
    @SerialName("message") val message: String? = null,
    @SerialName("transactions") val operations: List<OperationsHistoryItemResponse>? = null,
)

@Serializable
data class OperationsHistoryItemResponse(
    @SerialName("amount") val amount: String,
    @SerialName("type") val type: String,
    @SerialName("status") val status: String,
    @SerialName("created_at") val timestamp: String,
    @SerialName("appointment") val appointment: OperationsHistoryAppointmentResponse? = null,
    @SerialName("card") val cardResponse: OperationsHistoryTopUpCardResponse? = null,
)

@Serializable
data class OperationsHistoryAppointmentResponse(
    @SerialName("id") val id: Long,
    @SerialName("practitioner") val doctor: OperationsHistoryAppointmentDoctorResponse,
)

@Serializable
data class OperationsHistoryAppointmentDoctorResponse(
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
)

@Serializable
data class OperationsHistoryTopUpCardResponse(
    @SerialName("last4") val lastFourDigits: String,
    @SerialName("brand") val brand: String,
)