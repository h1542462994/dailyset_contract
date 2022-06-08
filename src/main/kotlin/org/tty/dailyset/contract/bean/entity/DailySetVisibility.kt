package org.tty.dailyset.contract.bean.entity

import org.tty.dailyset.contract.declare.ResourceSetVisibility

data class DailySetVisibility(
    override val uid: String,
    override val userUid: String,
    override val visible: Boolean
): ResourceSetVisibility