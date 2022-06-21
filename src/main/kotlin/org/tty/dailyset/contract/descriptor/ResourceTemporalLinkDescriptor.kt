package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.declare.ResourceConverter
import org.tty.dailyset.contract.declare.ResourceTemporalLink

interface ResourceTemporalLinkDescriptor<TE: Any, EC> {
    val converter: ResourceConverter<ResourceTemporalLink<EC>, TE>
}