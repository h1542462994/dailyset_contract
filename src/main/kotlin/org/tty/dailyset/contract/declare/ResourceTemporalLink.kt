package org.tty.dailyset.contract.declare

import java.time.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.tty.dailyset.contract.bean.serializer.LocalDateTimeIso8601Serializer

/**
 * **resource temporal link.** links the resource which is marked as *local*.
 */
@Serializable
data class ResourceTemporalLink<EC>(
    val setUid: String,
    val contentType: EC,
    val contentUid: String,
    val action: TemporalAction,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val lastTick: LocalDateTime
) {

    @Transient
    val key: LinkKey<EC> = LinkKey(setUid, contentType, contentUid)
}