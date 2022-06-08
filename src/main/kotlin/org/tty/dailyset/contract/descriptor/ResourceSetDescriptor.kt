package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.declare.ResourceConverter
import org.tty.dailyset.contract.declare.ResourceSet
import kotlin.reflect.KType

interface ResourceSetDescriptor<T: ResourceSet<ES>, TE, ES> {
    val type: KType
    val converter: ResourceConverter<T, TE>
}