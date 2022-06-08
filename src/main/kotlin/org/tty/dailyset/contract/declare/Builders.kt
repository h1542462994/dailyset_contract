package org.tty.dailyset.contract.declare

fun <T> resourceEquality(func: (first: T, second: T) -> Boolean): ResourceEquality<T> {
    return object: ResourceEquality<T> {
        override fun resourceEquals(first: T, second: T): Boolean {
            return func(first, second)
        }
    }
}

fun <T> resourceSameConverter(): ResourceConverter<T, T> {
    return object: ResourceConverter<T, T> {
        override fun convertFrom(entityBean: T): T {
            return entityBean
        }

        override fun convertTo(data: T): T {
            return data
        }
    }
}
