package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.enums.DailySetDurationType
import org.tty.dailyset.contract.bean.enums.DailySetPeriodCode
import org.tty.dailyset.contract.bean.serializer.LocalDate8601Serializer
import org.tty.dailyset.contract.declare.ResourceContent
import java.time.LocalDate

/**
 * dailyset content: duration
 */
@Serializable
@SerialName("duration")
data class DailySetDuration(
    override val uid: String,
    val type: DailySetDurationType,
    @Serializable(with = LocalDate8601Serializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDate8601Serializer::class)
    val endDate: LocalDate,
    val name: String,
    val bindingYear: Int,
    val bindingPeriodCode: DailySetPeriodCode
): ResourceContent, DailySetContent {
    override fun copyByUid(uid: String): ResourceContent = copy(uid = uid)
}