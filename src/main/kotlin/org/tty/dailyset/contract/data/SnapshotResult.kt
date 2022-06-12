package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet
import kotlinx.serialization.Serializable

/**
 * snapshot result of [ResourceSet], implementation is different between client and server.
 */
@Serializable
data class SnapshotResult<TS: ResourceSet<ES>,out TC: ResourceContent, ES, EC>(
    /**
     * set and it's version.
     */
    val set: TS,
    /**
     * typed resources.
     */
    val typedResources: List<TypedResources<TC, EC>>
)
