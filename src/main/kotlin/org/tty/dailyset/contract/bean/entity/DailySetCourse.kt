package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.enums.DailySetPeriodCode
import org.tty.dailyset.contract.declare.Key
import org.tty.dailyset.contract.declare.ResourceContent

@Serializable
@SerialName("course")
data class DailySetCourse(
    override val uid: String,
    val year: Int,
    val periodCode: DailySetPeriodCode,
    val name: String,
    val campus: String,
    val location: String,
    val teacher: String,
    val weeks: List<Int>,
    val weekDay: Int,
    val sectionStart: Int,
    val sectionEnd: Int,
    val digest: String
): ResourceContent, DailySetContent, Key<String> {
    override fun key(): String {
        return digest
    }

    override fun copyByUid(uid: String): ResourceContent = copy(uid = uid)
}