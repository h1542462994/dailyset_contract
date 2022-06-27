package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.declare.KeySelectorFunc
import org.tty.dailyset.contract.declare.defaultKeySelectorFunc

class ResourceDiff<T, TK>(
    sourceValues: List<T>,
    targetValues: List<T>,
    keySelector: KeySelectorFunc<T, TK>
) {
    data class Same<T, TK>(
        val sourceValue: T,
        val targetValue: T,
        val key: TK
    )

    private val sameList: MutableList<Same<T, TK>> = mutableListOf()
    private val addList: MutableList<T> = mutableListOf()
    private val removeList: MutableList<T> = mutableListOf()

    init {
        val sourceMap = sourceValues.associateBy { keySelector(it) }.toMutableMap()

        for (target in targetValues) {
            val key = keySelector(target)
            val source = sourceMap[key]
            if (source == null) {
                addList.add(target)
            } else {
                sourceMap.remove(key)
                sameList.add(Same(source, target, key))
            }
        }

        for (entry in sourceMap.entries) {
            removeList.add(entry.value)
        }

    }

    val sameValues: List<Same<T, TK>> get() = sameList
    val addValues: List<T> get() = addList
    val removeValues: List<T> get() = removeList

    companion object {
        fun <T, K> empty(): ResourceDiff<T, K> {
            return ResourceDiff(
                sourceValues = emptyList(),
                targetValues = emptyList(),
                keySelector = { throw IllegalStateException("empty resource diff is not supported.") }
            )
        }
    }
}