package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.annotation.ContentBean
import org.tty.dailyset.contract.bean.declare.ResourceContent
import org.tty.dailyset.contract.bean.enums.DailySetContentType
import org.tty.dailyset.contract.bean.serializer.LocalTimeIso8601Serializer
import java.time.LocalTime

/**
 * dailyset content: cell
 */
@ContentBean(DailySetContentType.Cell)
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
): ResourceContent, DailySetContent