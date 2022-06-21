package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent

data class ApplyingReq<out TC: ResourceContent, EC>(
    val setUid: String,
    val typedResourcesApplying: List<TypedResourcesApplying<TC, EC>>
)