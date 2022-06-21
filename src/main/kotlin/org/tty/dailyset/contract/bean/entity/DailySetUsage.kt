package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.enums.DailySetAuthType
import org.tty.dailyset.contract.declare.ResourceContent

/**
 * dailyset content: usage
 * @see [ResourceContent]
 */
@Serializable
@SerialName("usage")
data class DailySetUsage(
    override val uid: String,
    val setUid: String,
    val userUid: String,
    val authType: DailySetAuthType
): ResourceContent, DailySetContent {
    override fun copyByUid(uid: String): ResourceContent = copy(uid = uid)
}