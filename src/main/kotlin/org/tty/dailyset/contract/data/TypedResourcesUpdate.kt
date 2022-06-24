package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import kotlinx.serialization.Serializable

@Serializable
data class TypedResourcesUpdate<out TC: ResourceContent, EC>(
    val contentType: EC,
    val resourceContentsUn: List<ResourceContentUn<EC>>
)