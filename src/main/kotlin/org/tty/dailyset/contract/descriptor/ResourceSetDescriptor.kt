package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.declare.ResourceConverter
import org.tty.dailyset.contract.declare.ResourceSet
import kotlin.reflect.KType

interface ResourceSetDescriptor<TE: Any, ES> {
    val converter: ResourceConverter<ResourceSet<ES>, TE>
}