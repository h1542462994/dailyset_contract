package org.tty.dailyset.contract.bean.datatime

import java.time.LocalTime

/**
 * external local time, which has offset day (to some local data) and a localTime.
 */
data class ExLocalTime(
    val day: Int,
    val localTime: LocalTime
)