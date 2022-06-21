package org.tty.dailyset.contract.declare

/**
 * **resource content.** the resource content defined in sync module.
 */

interface ResourceContent {
    val uid: String

    fun copyByUid(uid: String): ResourceContent
}