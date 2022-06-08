package org.tty.dailyset.contract.bean.entity

import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.bean.enums.DailySetType
import kotlinx.serialization.Serializable

/**
 * dailyset. a specified resource set.
 * @see [ResourceSet]
 */
@Serializable
data class DailySet(
    override val uid: String,
    override val setType: DailySetType,
    override val version: Int
): ResourceSet<DailySetType>