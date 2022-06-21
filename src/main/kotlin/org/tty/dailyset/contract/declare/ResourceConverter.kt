package org.tty.dailyset.contract.declare

/**
 * a converter for resourceContent [T] from other [TE] bean.
 * it will be used if the real storage data (usually entity) differ from the data used in sync module, so this interface provides the converter for it.
 */
interface ResourceConverter<T, TE> {
    /**
     * convert form the storage data (entity) to the used data.
     */
    fun convertFrom(entityBean: @UnsafeVariance TE): T

    /**
     * convert from the used data to the storage data (entity).
     */
    fun convertTo(data: @UnsafeVariance T): TE
}