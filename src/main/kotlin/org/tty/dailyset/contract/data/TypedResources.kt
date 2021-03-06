package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import kotlinx.serialization.Serializable

/**
 * collection of typedResources
 * @see [SnapshotResult]
 */
@Serializable
data class TypedResources<out TC: ResourceContent, EC>(
    val contentType: EC,
    val resourceContents: List<TC>
)