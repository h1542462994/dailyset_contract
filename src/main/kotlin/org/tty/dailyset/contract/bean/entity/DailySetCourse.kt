package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.enums.DailySetPeriodCode
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceEquals

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
): ResourceContent, DailySetContent, ResourceEquals<DailySetCourse> {
    override fun resourceEqual(other: DailySetCourse): Boolean {
        return digest == other.digest
    }

    override fun copyByUid(uid: String): ResourceContent = copy(uid = uid)
}