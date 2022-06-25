package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.declare.ResourceConverter
import org.tty.dailyset.contract.declare.ResourceTemporaryLink

interface ResourceTemporaryLinkDescriptor<TE: Any, EC> {
    val converter: ResourceConverter<ResourceTemporaryLink<EC>, TE>
}