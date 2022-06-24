package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceTemporalLink

import kotlinx.serialization.Serializable

@Serializable
data class ResourceContentTn<out TC: ResourceContent, EC>(
    val temporalLink: ResourceTemporalLink<EC>,
    val content: TC?
)