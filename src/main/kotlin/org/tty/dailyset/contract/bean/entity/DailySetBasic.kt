package org.tty.dailyset.contract.bean.entity

import org.tty.dailyset.contract.bean.annotation.ContentBean
import org.tty.dailyset.contract.bean.declare.ResourceContent
import org.tty.dailyset.contract.bean.enums.DailySetContentType

@ContentBean(DailySetContentType.Basic)
data class DailySetBasic(
    override val uid: String,
    val name: String,
    val icon: String,
): ResourceContent