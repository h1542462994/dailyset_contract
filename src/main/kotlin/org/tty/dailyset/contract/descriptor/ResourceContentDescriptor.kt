package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.declare.KeySelector
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceConverter
import kotlin.reflect.KType

/**
 * the describer for a [ResourceContent].
 * @param T data type.
 * @param TE entity data type (storage).
 * @param EC enum for resource content.
 * @see ResourceContent
 */
sealed interface ResourceContentDescriptor<T: ResourceContent, TE: Any, EC> {
    val type: KType
    val contentType: EC
    val keySelector: KeySelector<T, Any>
    val converter: ResourceConverter<T, TE>
}