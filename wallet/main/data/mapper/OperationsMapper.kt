package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.mapper

import com.bitcot.saludnow.client.core.extensions.orZero
import com.bitcot.saludnow.client.core.utils.Timestamp
import com.bitcot.saludnow.client.core.utils.enumOf
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.domain.card.model.CardBrand
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.OperationsHistoryItemResponse
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.OperationsHistoryResponse
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.CancellationInitiator
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.Operation

fun OperationsHistoryResponse.toResult(): List<Operation> =
    this.operations?.map { it.toResult() }?.filterNot { it is Operation.Unknown } ?: emptyList()

fun OperationsHistoryItemResponse.toResult(): Operation =
    when (this.type) {
        "top-up.card" -> Operation.TopUp.BankCard(
            amount = this.amount.toDoubleOrNull().orZero().toInt(),
            timestamp = Timestamp(this.timestamp),
            brand = enumOf(this.cardResponse?.brand.orEmpty(), CardBrand.UNKNOWN),
            lastFourDigits = this.cardResponse?.lastFourDigits.orEmpty(),
        )

        "top-up.oxxo" -> Operation.TopUp.Oxxo(
            amount = this.amount.toDoubleOrNull().orZero().toInt(),
            timestamp = Timestamp(this.timestamp),
        )

        "payment.card", "payment.oxxo", "payment.wallet" -> Operation.AppointmentPurchase(
            amount = this.amount.toDoubleOrNull().orZero().toInt(),
            timestamp = Timestamp(this.timestamp),
            appointmentId = this.appointment?.id ?: -1,
            doctorFullName = "${this.appointment?.doctor?.firstName} ${this.appointment?.doctor?.lastName}"
        )

        "promo" -> Operation.TopUp.PromoCode(
            amount = this.amount.toDoubleOrNull().orZero().toInt(),
            timestamp = Timestamp(this.timestamp),
        )

        "refund.cancel-by-client" -> Operation.AppointmentCancellationRefund(
            amount = this.amount.toDoubleOrNull().orZero().toInt(),
            timestamp = Timestamp(this.timestamp),
            appointmentId = this.appointment?.id ?: -1,
            doctorFullName = "${this.appointment?.doctor?.firstName} ${this.appointment?.doctor?.lastName}",
            initiator = CancellationInitiator.PATIENT,
        )

        "refund.cancel-by-doctor" -> Operation.AppointmentCancellationRefund(
            amount = this.amount.toDoubleOrNull().orZero().toInt(),
            timestamp = Timestamp(this.timestamp),
            appointmentId = this.appointment?.id ?: -1,
            doctorFullName = "${this.appointment?.doctor?.firstName} ${this.appointment?.doctor?.lastName}",
            initiator = CancellationInitiator.DOCTOR,
        )

        else -> Operation.Unknown(
            amount = this.amount.toDoubleOrNull().orZero().toInt(),
            timestamp = Timestamp(this.timestamp),
        )
    }