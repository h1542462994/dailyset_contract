package org.tty.dailyset.contract.data

import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.declare.ResourceContent

/**
 * wrapped [ResourceContent] and [InAction], represents the action of element applying.
 * it is used in [TypedResourcesApplying]
 * @see TypedResourcesApplying
 * @see ApplyingReq
 */
@Serializable
data class ResourceContentIn<out TC: ResourceContent>(
    val action: InAction,
    val resourceContent: TC?
)