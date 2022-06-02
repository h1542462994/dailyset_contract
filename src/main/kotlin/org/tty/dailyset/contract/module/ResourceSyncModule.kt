package org.tty.dailyset.contract.module

import org.tty.dailyset.contract.bean.enums.DailySetContentType
import kotlin.reflect.KType

interface ResourceSyncModule<TS, TC> {
    fun registerContentType(vararg types: Pair<DailySetContentType, KType>)

}