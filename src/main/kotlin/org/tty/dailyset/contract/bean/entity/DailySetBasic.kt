package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.SerialName
import org.tty.dailyset.contract.bean.annotation.ContentBean
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.bean.enums.DailySetContentType
import kotlinx.serialization.Serializable

/**
 * dailyset content: basic.
 * @see [ResourceContent]
 */
@ContentBean(DailySetContentType.Basic)
@Serializable
@SerialName("basic")
data class DailySetBasic(
    override val uid: String,
    val name: String,
    val icon: String,
): ResourceContent, DailySetContent