package org.tty.dailyset.contract.test.vocal.bean

import kotlinx.serialization.Serializable
import kotlinx.serialization.Polymorphic
import org.tty.dailyset.contract.declare.ResourceContent

@Serializable
@Polymorphic
sealed interface VocalContent: ResourceContent