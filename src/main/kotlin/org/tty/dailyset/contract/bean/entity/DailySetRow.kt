package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import org.tty.dailyset.contract.bean.annotation.ContentBean
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.bean.enums.DailySetContentType
import kotlinx.serialization.Serializable

/**
 * dailyset content: row
 * @see [ResourceContent]
 */
@ContentBean(DailySetContentType.Row)
@Serializable
@SerialName("row")
data class DailySetRow(
    override val uid: String,
    val tableUid: String,
    val currentIndex: Int,
    val weekdays: List<Int>,
    val counts: List<Int>
): ResourceContent, DailySetContent