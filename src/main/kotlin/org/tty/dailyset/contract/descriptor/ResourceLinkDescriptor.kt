package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.declare.ResourceConverter
import org.tty.dailyset.contract.declare.ResourceLink
import kotlin.reflect.KType

interface ResourceLinkDescriptor<T: ResourceLink<EC>, TE, EC> {
    val type: KType
    val converter: ResourceConverter<T, TE>
}