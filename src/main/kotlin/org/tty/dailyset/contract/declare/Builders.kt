package org.tty.dailyset.contract.declare


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
