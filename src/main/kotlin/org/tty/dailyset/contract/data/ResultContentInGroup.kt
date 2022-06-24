package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent

data class ResultContentInGroup<out TC: ResourceContent>(
    val action: InAction,
    val contents: List<TC>
)
