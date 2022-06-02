package org.tty.dailyset.contract.bean.entity

import org.tty.dailyset.contract.bean.declare.ResourceLink
import org.tty.dailyset.contract.bean.enums.DailySetContentType

data class DailySetLink(
    override val setUid: String,
    override val contentType: DailySetContentType,
    override val contentUid: String,
    override val version: Int,
    override val isRemoved: Boolean
): ResourceLink<DailySetContentType>