package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model

import com.bitcot.saludnow.client.core.utils.Timestamp
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.domain.card.model.CardBrand

sealed interface Operation {
    val amount: Int
    val timestamp: Timestamp

    sealed interface TopUp : Operation {
        data class BankCard(
            override val amount: Int,
            override val timestamp: Timestamp,
            val brand: CardBrand,
            val lastFourDigits: String,
        ) : TopUp

        data class Oxxo(
            override val amount: Int,
            override val timestamp: Timestamp,
        ) : TopUp

        data class PromoCode(
            override val amount: Int,
            override val timestamp: Timestamp,
        ) : TopUp
    }

    data class BundlePurchase(
        override val amount: Int,
        override val timestamp: Timestamp,
        val type: BundleType,
    ) : Operation

    data class AppointmentPurchase(
        override val amount: Int,
        override val timestamp: Timestamp,
        val appointmentId: Long,
        val doctorFullName: String,
    ) : Operation

    data class AppointmentCancellationRefund(
        override val amount: Int,
        override val timestamp: Timestamp,
        val appointmentId: Long,
        val doctorFullName: String,
        val initiator: CancellationInitiator,
    ) : Operation

    data class Unknown(
        override val amount: Int,
        override val timestamp: Timestamp,
    ) : Operation
}

enum class BundleType {
    LITE,
    MEDIUM,
    PREMIUM,
}

enum class CancellationInitiator {
    DOCTOR,
    PATIENT,
}