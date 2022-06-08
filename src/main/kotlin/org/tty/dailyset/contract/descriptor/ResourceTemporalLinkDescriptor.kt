package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.declare.ResourceConverter
import org.tty.dailyset.contract.declare.ResourceTemporalLink
import kotlin.reflect.KType

interface ResourceTemporalLinkDescriptor<T: ResourceTemporalLink<EC>, TE, EC> {
    val type: KType
    val converter: ResourceConverter<T, TE>
}