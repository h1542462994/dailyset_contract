package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*

data class DescriptorSetSync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TC: ResourceContent, TV: ResourceSetVisibility, ES, EC>(
    val setDescriptor: ResourceSetDescriptorSync<TS, *, ES>,
    val linkDescriptor: ResourceLinkDescriptorSync<TL, *, EC>,
    val temporalLinkDescriptor: ResourceTemporalLinkDescriptorSync<TTL, *, EC>,
    val contentDescriptors: List<ResourceContentDescriptorSync<out TC, *, EC>>,
    val setVisibilityDescriptor: ResourceSetVisibilityDescriptorSync<TV, *>
)