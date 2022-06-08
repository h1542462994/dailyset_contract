package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.declare.ResourceConverter
import org.tty.dailyset.contract.declare.ResourceSetVisibility
import kotlin.reflect.KType

interface ResourceSetVisibilityDescriptor<T: ResourceSetVisibility, TE> {
    val type: KType
    val converter: ResourceConverter<T, TE>
}