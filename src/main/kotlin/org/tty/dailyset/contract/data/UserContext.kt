package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceDefaults

class UserContext(
    val userUid: String
) {
    companion object {
        val EMPTY = UserContext(ResourceDefaults.EMPTY_UID)
    }
}