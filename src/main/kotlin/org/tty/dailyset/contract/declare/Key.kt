package org.tty.dailyset.contract.declare

/**
 * market the key of the resource in comparison.
 */
interface Key<out T> {
    fun key(): T
}

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
            value as TK
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

