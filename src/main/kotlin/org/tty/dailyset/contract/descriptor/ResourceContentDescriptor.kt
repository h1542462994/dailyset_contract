package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.declare.KeySelectorFunc
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceConverter

/**
 * the describer for a [ResourceContent].
 * @param T data type.
 * @param TE entity data type (storage).
 * @param EC enum for resource content.
 * @see ResourceContent
 */
sealed interface ResourceContentDescriptor<T: ResourceContent, TE: Any, EC> {
    val contentType: EC
    val keySelector: KeySelectorFunc<T, Any>
    val converter: ResourceConverter<T, TE>
}