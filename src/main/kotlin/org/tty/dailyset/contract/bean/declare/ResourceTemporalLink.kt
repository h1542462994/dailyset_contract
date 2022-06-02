package org.tty.dailyset.contract.bean.declare

import org.tty.dailyset.contract.bean.enums.TemporalAction

/**
 * **resource temporal link.** links the resource which is marked as *local*.
 */
interface ResourceTemporalLink<EC> {
    val setUid: String
    val contentType: EC
    val contentUid: String
    val action: TemporalAction
}