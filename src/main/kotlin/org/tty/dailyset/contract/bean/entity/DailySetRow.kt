package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.declare.ResourceContent

/**
 * dailyset content: row
 * @see [ResourceContent]
 */
@Serializable
@SerialName("row")
data class DailySetRow(
    override val uid: String,
    val tableUid: String,
    val currentIndex: Int,
    val weekdays: List<Int>,
    val counts: List<Int>
): ResourceContent, DailySetContent {
    override fun copyByUid(uid: String): ResourceContent = copy(uid = uid)
}