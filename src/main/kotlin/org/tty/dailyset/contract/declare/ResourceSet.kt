package org.tty.dailyset.contract.declare

/**
 * **resource set.** the set of the resource contents. it holds multi resource link.
 * @see ResourceContent
 */
interface ResourceSet<ES> {
    val uid: String
    val setType: ES
    val version: Int
}