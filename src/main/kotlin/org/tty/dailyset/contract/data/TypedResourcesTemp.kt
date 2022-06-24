package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import kotlinx.serialization.Serializable

@Serializable
data class TypedResourcesTemp<out TC: ResourceContent, EC>(
    val contentType: EC,
    val resourceContentsTn: List<ResourceContentTn<TC, EC>>
)