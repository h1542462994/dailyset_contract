package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceSet

data class Relating<TS: ResourceSet<ES>, ES>(
    val sets: List<TS>
)