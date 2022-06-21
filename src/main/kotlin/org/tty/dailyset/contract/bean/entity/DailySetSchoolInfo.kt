package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.declare.ResourceContent

/**
 * dailyset content: school info
 * @see [ResourceContent]
 */
@Serializable
@SerialName("schoolInfo")
data class DailySetSchoolInfo(
    override val uid: String,
    val identifier: String,
    val name: String
): ResourceContent, DailySetContent {
    override fun copyByUid(uid: String): ResourceContent = copy(uid = uid)
}