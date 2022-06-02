package org.tty.dailyset.contract.bean.entity

import org.tty.dailyset.contract.bean.declare.ResourceSet
import org.tty.dailyset.contract.bean.enums.DailySetType

/**
 *
 */
data class DailySet(
    override val uid: String,
    override val setType: DailySetType,
    override val version: Int
): ResourceSet<DailySetType>