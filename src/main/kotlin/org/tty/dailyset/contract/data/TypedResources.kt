package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import kotlinx.serialization.Serializable

@Serializable
data class TypedResources<TC: ResourceContent, EC>(
    val contentType: EC,
    val resourceContents: List<TC>
)