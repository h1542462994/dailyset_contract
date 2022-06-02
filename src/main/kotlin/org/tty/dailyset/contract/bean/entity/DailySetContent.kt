package org.tty.dailyset.contract.bean.entity

import kotlinx.serialization.Polymorphic
import org.tty.dailyset.contract.bean.declare.ResourceContent
import kotlinx.serialization.Serializable

/**
 * dailyset content
 */
@Serializable
@Polymorphic
sealed interface DailySetContent: ResourceContent