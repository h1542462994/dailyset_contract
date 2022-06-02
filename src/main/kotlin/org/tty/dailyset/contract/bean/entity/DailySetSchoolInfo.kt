package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import org.tty.dailyset.contract.bean.annotation.ContentBean
import org.tty.dailyset.contract.bean.declare.ResourceContent
import org.tty.dailyset.contract.bean.enums.DailySetContentType
import kotlinx.serialization.Serializable

/**
 * dailyset content: school info
 * @see [ResourceContent]
 */
@ContentBean(DailySetContentType.SchoolInfo)
@Serializable
@SerialName("schoolInfo")
data class DailySetSchoolInfo(
    override val uid: String,
    val identifier: String,
    val name: String
): ResourceContent, DailySetContent