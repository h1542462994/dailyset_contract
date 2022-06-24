package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.declare.ResourceTemporalLink
import kotlinx.serialization.Serializable

/**
 * a snapshot for newest visitable data in **client** or **server**.
 *
 * in **server**, it mean the storage data, because [ResourceTemporalLink] is not included.
 *
 * in **client**, it means the storage **download** or **temporal** data.
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

    operator fun get(contentType: EC): TypedResources<TC, EC> {
        return typedResources.find { it.contentType == contentType }
            ?: TypedResources(contentType, listOf())
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: ResourceContent> getVariance(contentType: EC): TypedResources<T, EC> {
        return typedResources.find { it.contentType == contentType } as TypedResources<T, EC>?
            ?: TypedResources(contentType, listOf())
    }
}
