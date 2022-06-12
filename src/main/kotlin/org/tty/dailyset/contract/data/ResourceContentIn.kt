package org.tty.dailyset.contract.data

import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.enums.InAction
import org.tty.dailyset.contract.declare.ResourceContent

@Serializable
data class ResourceContentIn<out TC: ResourceContent>(
    val action: InAction,
    val resourceContent: TC?
)