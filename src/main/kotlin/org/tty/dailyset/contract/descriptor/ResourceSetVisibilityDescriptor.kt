package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.declare.ResourceConverter
import org.tty.dailyset.contract.declare.ResourceSetVisibility

interface ResourceSetVisibilityDescriptor<TE: Any> {
    val converter: ResourceConverter<ResourceSetVisibility, TE>
}