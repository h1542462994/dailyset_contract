package org.tty.dailyset.contract.declare

import org.tty.dailyset.contract.descriptor.ResourceContentDescriptor

/**
 * the key of the [ResourceContent].
 *
 * it is used to locate the resourceContent when **uid** is empty.
 *
 * @see KeySelector
 */
interface Key<out T> {
    fun key(): T
}

/**
 * the keySelector for module.
 *
 * it is used to locate the resourceContent when **uid** is empty.
 *
 * **default impl** is the [Key] on [ResourceContent], or you can provide [ProvideKeySelector] in [ResourceContentDescriptor]
 */
interface KeySelector< T, out TK> {
    fun selectKey(value: @UnsafeVariance T): TK
}

typealias KeySelectFunc<T, TK> = (value: T) -> TK

class DefaultKeySelector<T, TK>: KeySelector<T, TK> {
    @Suppress("UNCHECKED_CAST")
    override fun selectKey(value: T): TK {
        return if (value is Key<*>) {
            value.key() as TK
        } else {
            throw IllegalStateException("ResourceContent not implemented Key interface, so we couldn't locate the resource by content, please impl with Key or use ProvideKeySelector instead.")
        }
    }
}

class ProvideKeySelector<T, TK>(
    private val func: KeySelectFunc<T, TK>
): KeySelector<T, TK> {
    override fun selectKey(value: T): TK {
        return func(value)
    }
}

