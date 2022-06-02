package org.tty.dailyset.contract.bean.declare

/**
 * **resource links.** links the resource which is maintained by sync module.
 */
interface ResourceLink<EC> {
    val setUid: String
    val contentType: EC
    val contentUid: String
    val version: Int
    val isRemoved: Boolean
}