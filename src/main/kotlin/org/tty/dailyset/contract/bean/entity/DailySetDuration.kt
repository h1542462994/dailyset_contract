package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.bean.enums.DailySetDurationType
import org.tty.dailyset.contract.bean.enums.DailySetPeriodCode
import java.time.LocalDate
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.annotation.ContentBean
import org.tty.dailyset.contract.bean.enums.DailySetContentType
import org.tty.dailyset.contract.bean.serializer.LocalDate8601Serializer

/**
 * dailyset content: duration
 */
@ContentBean(DailySetContentType.Duration)
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
): ResourceContent, DailySetContent