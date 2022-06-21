package org.tty.dailyset.contract.declare

/**
 * represent the resource is equal.
 * it only means they are equal in resource facet.
 */
interface ResourceEquals<T> {
    fun resourceEqual(other: T): Boolean
}

/**
 * represent the resource is equal. (equality is an extension)
 * it only means they are equal in resource facet.
 */
typealias ResourceEquality<T> = (first: T, second: T) -> Boolean