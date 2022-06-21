package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.declare.ResourceContent

/**
 * dailyset content: basic.
 * @see [ResourceContent]
 */
@Serializable
@SerialName("basic")
data class DailySetBasic(
    override val uid: String,
    val name: String,
    val icon: String,
): ResourceContent, DailySetContent {
    override fun copyByUid(uid: String): ResourceContent = copy(uid = uid)
}