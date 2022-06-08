package org.tty.dailyset.contract.declare

/**
 * represent the resource is equal.
 * it only means they are equal in resource facet.
 */
interface ResourceEquals<T> {
    fun resourceEqual(other: T): Boolean
}