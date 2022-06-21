package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*

data class DescriptorSetSync<TC: ResourceContent, ES, EC>(
    val setDescriptor: ResourceSetDescriptorSync<*, ES>,
    val linkDescriptor: ResourceLinkDescriptorSync<*, EC>,
    val temporalLinkDescriptor: ResourceTemporalLinkDescriptorSync<*, EC>,
    val contentDescriptors: List<ResourceContentDescriptorSync<out TC, *, EC>>,
    val setVisibilityDescriptor: ResourceSetVisibilityDescriptorSync<*>
)