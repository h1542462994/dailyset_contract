package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.annotation.ContentBean
import org.tty.dailyset.contract.bean.declare.ResourceContent
import org.tty.dailyset.contract.bean.enums.DailySetAuthType
import org.tty.dailyset.contract.bean.enums.DailySetContentType

/**
 * dailyset content: usage
 * @see [ResourceContent]
 */
@ContentBean(DailySetContentType.Usage)
@Serializable
@SerialName("usage")
data class DailySetUsage(
    override val uid: String,
    val setUid: String,
    val userUid: String,
    val authType: DailySetAuthType
): ResourceContent, DailySetContent