package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceLink
import kotlinx.serialization.Serializable

@Serializable
data class ResourceContentUn<EC>(
    val link: ResourceLink<EC>,
    val content: ResourceContent
)