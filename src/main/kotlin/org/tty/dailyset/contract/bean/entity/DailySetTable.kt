package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.declare.ResourceContent

/**
 * dailyset content: table
 * @see [ResourceContent]
 */
@Serializable
@SerialName("table")
data class DailySetTable(
    override val uid: String,
    val name: String
): ResourceContent, DailySetContent {
    override fun copyByUid(uid: String): ResourceContent = copy(uid = uid)
}