package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import kotlinx.serialization.Serializable

/**
 * collection for typedResources and action which is used in [ApplyingReq].
 * @see ApplyingReq
 */

@Serializable
data class TypedResourcesApplying<out TC: ResourceContent, EC>(
    val contentType: EC,
    val resourceContentsIn: List<ResourceContentIn<TC>>
)