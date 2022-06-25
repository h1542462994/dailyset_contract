package org.tty.dailyset.contract.declare

/**
 * the key of the [ResourceContent].
 *
 * it is used to locate the resourceContent when **uid** is empty.
 *
 * @see KeySelectorFunc
 */
interface Key<out T> {
    fun key(): T
}

/**
 * the keySelector for module.
 *
 * it is used to locate the resourceContent when **uid** is empty.
 *
 * **default impl** is the [Key] on [ResourceContent], or you can provide it.
 * @see [defaultKeySelectorFunc]
 */
typealias KeySelectorFunc<T, TK> = (value: T) -> TK

fun <T, TK> defaultKeySelectorFunc(): KeySelectorFunc<T, TK> {
    return { value: T ->
        if (value is Key<*>) {
            @Suppress("UNCHECKED_CAST")
            value.key() as TK
        } else {
            throw IllegalStateException("ResourceContent not implemented Key interface, so we couldn't locate the resource by content, please impl with Key or use ProvideKeySelector instead.")
        }
    }
}

