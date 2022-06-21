package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.declare.ResourceContent

/**
 * dailyset content: student info.
 * @see [ResourceContent]
 */
@Serializable
@SerialName("studentInfo")
data class DailySetStudentInfo(
    override val uid: String,
    val collegeName: String,
    val className: String,
    val name: String,
    val inviteYear: Int,
    val graduationYear: Int,
): ResourceContent, DailySetContent {
    override fun copyByUid(uid: String): ResourceContent = copy(uid = uid)
}