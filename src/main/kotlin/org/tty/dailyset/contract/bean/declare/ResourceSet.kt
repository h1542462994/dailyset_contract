package org.tty.dailyset.contract.bean.declare

/**
 * **resource set.** the set of the resource contents.
 * @see ResourceContent
 */
interface ResourceSet<ES> {
    val uid: String
    val setType: ES
    val version: Int
}