package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet
import kotlinx.serialization.Serializable

/**
 * snapshot result of [ResourceSet], implementation is different between client and server.
 * snapshot will only contain [set] and valid [typedResources]
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
}
