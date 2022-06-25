package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.declare.KeySelectorFunc

class ResourceDiff2<T1, T2, TK>(
    sourceValues: List<T1>,
    targetValues: List<T2>,
    sourceKeySelector: KeySelectorFunc<T1, TK>,
    targetKeySelector: KeySelectorFunc<T2, TK>
) {
    data class Same<T1, T2, TK>(
        val sourceValue: T1,
        val targetValue: T2,
        val key: TK
    )

    private val sameList: MutableList<Same<T1, T2, TK>> = mutableListOf()
    private val addList: MutableList<T2> = mutableListOf()
    private val removeList: MutableList<T1> = mutableListOf()

    init {
        val sourceMap = sourceValues.associateBy { sourceKeySelector(it) }.toMutableMap()

        for (target in targetValues) {
            val key = targetKeySelector(target)
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

    val sameValues: List<Same<T1, T2, TK>> get() = sameList
    val addValues: List<T2> get() = addList
    val removeValues: List<T1> get() = removeList

}