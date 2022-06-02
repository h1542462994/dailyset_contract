package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.annotation.ContentBean
import org.tty.dailyset.contract.bean.declare.ResourceContent
import org.tty.dailyset.contract.bean.enums.DailySetContentType

/**
 * dailyset content: table
 * @see [ResourceContent]
 */
@ContentBean(DailySetContentType.Table)
@Serializable
@SerialName("table")
data class DailySetTable(
    override val uid: String,
    val name: String
): ResourceContent, DailySetContent