package org.tty.dailyset.contract.bean.entity

import org.tty.dailyset.contract.bean.declare.ResourceLink
import org.tty.dailyset.contract.bean.enums.DailySetContentType
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.serializer.LocalDateTimeIso8601Serializer
import java.time.LocalDateTime

/**
 * dailyset link. a specific link.
 * @see [ResourceLink]
 */
@Serializable
data class DailySetLink(
    override val setUid: String,
    override val contentType: DailySetContentType,
    override val contentUid: String,
    override val version: Int,
    override val isRemoved: Boolean,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    override val lastTick: LocalDateTime
): ResourceLink<DailySetContentType>