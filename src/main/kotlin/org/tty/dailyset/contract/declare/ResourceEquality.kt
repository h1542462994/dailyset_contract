package org.tty.dailyset.contract.declare

/**
 * represent the resource is equal. (equality is an extension)
 * it only means they are equal in resource facet.
 */
interface ResourceEquality<T> {
    fun resourceEquals(first: T, second: T): Boolean
}