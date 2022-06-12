package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet

data class ApplyingResult<TS: ResourceSet<ES>, out TC: ResourceContent, ES, EC>(
    val set: TS,
    val typedResourcesApplying: List<TypedResourcesApplying<TC, EC>>
)