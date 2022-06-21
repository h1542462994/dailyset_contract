package org.tty.dailyset.contract.declare

import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.serializer.LocalDateTimeIso8601Serializer
import java.time.LocalDateTime

/**
 * **resource links.** links the resource which is maintained by sync module.
 */
@Serializable
data class ResourceLink<EC>(
    val setUid: String,
    val contentType: EC,
    val contentUid: String,
    val version: Int,
    val isRemoved: Boolean,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val lastTick: LocalDateTime
)