package org.tty.dailyset.contract.bean.entity

import org.tty.dailyset.contract.bean.declare.ResourceTemporalLink
import org.tty.dailyset.contract.bean.enums.DailySetContentType
import org.tty.dailyset.contract.bean.enums.TemporalAction

data class DailySetTemporalLink(
    override val setUid: String,
    override val contentType: DailySetContentType,
    override val contentUid: String,
    override val action: TemporalAction
): ResourceTemporalLink<DailySetContentType>