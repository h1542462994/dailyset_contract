package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceSet

data class Relating<ES>(
    val sets: List<ResourceSet<ES>>
)

fun <ES> combines(relatings: Iterable<Relating<ES>>): Relating<ES> {
    val internalSets = buildList {
        relatings.forEach {
            addAll(it.sets)
        }
    }
    return Relating(sets = internalSets.distinct())
}

