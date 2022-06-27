package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.declare.ResourceTemporaryLink
import kotlinx.serialization.Serializable

/**
 * a snapshot for newest visitable data in **client** or **server**.
 *
 * in **server**, it mean the storage data, because [ResourceTemporaryLink] is not included.
 *
 * in **client**, it means the storage **download** or **temporary** data.
 *
 * the data is used for **visualization** or **proceed**, so it is not recommend for transport.
 *
 * it is the return of *ResourceSyncModule:read*.
 */
@Serializable
data class SnapshotResult<out TC: ResourceContent, ES, EC>(
    /**
     * set and it's version.
     */
    val set: ResourceSet<ES>,
    /**
     * typed resources.
     */
    val typedResources: List<TypedResources<TC, EC>>

) {

    operator fun get(contentType: EC): List<TC> {
        return typedResources.find { it.contentType == contentType }?.resourceContents
            ?: emptyList()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: ResourceContent> getVariance(contentType: EC): List<T> {
        return this[contentType] as List<T>
    }
}
