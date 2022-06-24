package org.tty.dailyset.contract.declare

object ResourceDefaults {
    /**
     * version zero. it is used on fetch whole data. or mark as a not updated set in client.
     */
    const val VERSION_ZERO = 0

    /**
     * version init, it is used on initialize of the set in server.
     */
    const val VERSION_INIT = 1

    /**
     * resourceContent is mark as local use [LOCAL_SUFFIX] after the uid.
     */
    const val LOCAL_SUFFIX = ".&local"
    const val EMPTY_UID = ""
}