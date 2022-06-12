package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent

data class TypedResourcesApplying<out TC: ResourceContent, EC>(
    val contentType: EC,
    val resourceContentsIn: List<ResourceContentIn<TC>>
)