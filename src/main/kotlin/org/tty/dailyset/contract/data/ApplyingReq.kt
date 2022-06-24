package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import kotlinx.serialization.Serializable

@Serializable
data class ApplyingReq<out TC: ResourceContent, EC>(
    val setUid: String,
    val typedResourcesApplying: List<TypedResourcesApplying<TC, EC>>
)