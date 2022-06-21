package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.serializer.LocalTimeIso8601Serializer
import org.tty.dailyset.contract.declare.ResourceContent
import java.time.LocalTime

/**
 * dailyset content: cell
 */
@Serializable
@SerialName("cell")
data class DailySetCell(
    override val uid: String,
    val rowUid: String,
    val currentIndex: Int,
    @Serializable(with = LocalTimeIso8601Serializer::class)
    val startTime: LocalTime,
    @Serializable(with = LocalTimeIso8601Serializer::class)
    val endTime: LocalTime,
    val normalType: Int,
    val normalIndex: Int
): ResourceContent, DailySetContent {
    override fun copyByUid(uid: String): ResourceContent = copy(uid = uid)
}