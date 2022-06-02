package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.annotation.ContentBean
import org.tty.dailyset.contract.bean.declare.ResourceContent
import org.tty.dailyset.contract.bean.enums.DailySetContentType

/**
 * dailyset content: student info.
 * @see [ResourceContent]
 */
@ContentBean(DailySetContentType.StudentInfo)
@Serializable
@SerialName("studentInfo")
data class DailySetStudentInfo(
    override val uid: String,
    val collegeName: String,
    val className: String,
    val name: String,
    val inviteYear: Int,
    val graduationYear: Int,
): ResourceContent, DailySetContent