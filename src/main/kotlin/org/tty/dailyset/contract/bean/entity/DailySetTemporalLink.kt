package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.declare.ResourceTemporalLink
import org.tty.dailyset.contract.bean.enums.DailySetContentType
import org.tty.dailyset.contract.bean.enums.TemporalAction
import org.tty.dailyset.contract.bean.serializer.LocalDateTimeIso8601Serializer
import java.time.LocalDateTime

/**
 * dailyset temporal link. a specified temporal link.
 * @see [ResourceTemporalLink]
 */
@Serializable
data class DailySetTemporalLink(
    override val setUid: String,
    override val contentType: DailySetContentType,
    override val contentUid: String,
    override val action: TemporalAction,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    override val lastTick: LocalDateTime
): ResourceTemporalLink<DailySetContentType>