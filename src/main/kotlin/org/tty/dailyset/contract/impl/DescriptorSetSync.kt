package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*

internal data class DescriptorSetSync<TC: ResourceContent, ES, EC>(
    val setDescriptor: ResourceSetDescriptorSync<*, ES>,
    val linkDescriptor: ResourceLinkDescriptorSync<*, EC>,
    val temporaryLinkDescriptor: ResourceTemporaryLinkDescriptorSync<*, EC>?,
    val contentDescriptors: List<ResourceContentDescriptorSync<out TC, *, EC>>,
    val setVisibilityDescriptor: ResourceSetVisibilityDescriptorSync<*>?
)