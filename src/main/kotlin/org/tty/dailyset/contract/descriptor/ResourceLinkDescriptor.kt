package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.declare.ResourceConverter
import org.tty.dailyset.contract.declare.ResourceLink
import kotlin.reflect.KType

interface ResourceLinkDescriptor<TE: Any, EC> {
    val converter: ResourceConverter<ResourceLink<EC>, TE>
}