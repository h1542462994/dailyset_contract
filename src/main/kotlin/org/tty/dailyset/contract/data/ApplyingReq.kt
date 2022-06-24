package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import kotlinx.serialization.Serializable

/**
 * used for apply(write) data to sync module.
 *
 * in **server**, it means apply new data to sync module, and **increase version**.
 *
 * in **client**, it means apply temporal data to sync module, it will be used as the data **upload** to server.
 *
 * **notice**: data with be observed by *ResourceSyncModule::read*
 *
 * you could create this by factory method [applyingReq].
 *
 * used in *ResourceSyncModule::write*
 *
 * the unit bean is [ResourceContentIn], which contains [InAction]
 *
 * @see [applyingReq]
 * @see [ApplyReqBuilder]
 * @see [InAction]
 */
@Serializable
data class ApplyingReq<out TC: ResourceContent, EC>(
    val setUid: String,
    val typedResourcesApplying: List<TypedResourcesApplying<TC, EC>>
)