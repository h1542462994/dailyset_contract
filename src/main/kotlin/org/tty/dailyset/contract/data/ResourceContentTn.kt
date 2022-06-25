package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceTemporaryLink

import kotlinx.serialization.Serializable

@Serializable
data class ResourceContentTn<out TC: ResourceContent, EC>(
    val temporaryLink: ResourceTemporaryLink<EC>,
    val content: TC?
)