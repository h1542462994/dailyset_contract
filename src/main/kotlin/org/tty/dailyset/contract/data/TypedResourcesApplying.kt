package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import kotlinx.serialization.Serializable

/**
 * collection for contentType and [ResourceContentIn]
 * @see ApplyingReq
 */

@Serializable
data class TypedResourcesApplying<out TC: ResourceContent, EC>(
    val contentType: EC,
    val resourceContentsIn: List<ResourceContentIn<TC>>
) {
    fun prelude(): List<ResultContentInGroup<TC>> {
        val actionList = mutableListOf<Pair<InAction, MutableList<TC>>>()
        var replaceMode = false
        var singleMode = false

        fun requireNormal() {
            require(!replaceMode) { "other action is not supported in InAction.Replace mode." }
            require(!singleMode) { "other action is not supported in InAction.Single mode" }
        }
        fun requireSingle() {
            require(actionList.isEmpty()) {
                "other action is not supported in InAction.Single mode. or have multi elements."
            }
        }
        fun requireRemoveAll() {
            require(actionList.isEmpty()) {
                "InAction.ReplaceAll is must be first element. and couldn't occur more than once."
            }
        }
        fun requireReplaceMode() {
            require(actionList.isEmpty() || (actionList.size == 1 && actionList[0].first == InAction.Replace)) {
                "other action is not supported in InAction.Replace mode."
            }
        }

        for(index in resourceContentsIn.indices) {
            val resourceContentIn = resourceContentsIn[index]
            if (resourceContentIn.action == InAction.Single) {
                requireSingle()
                singleMode = true
                actionList.add(Pair(InAction.Single, mutableListOf(resourceContentIn.resourceContent!!)))
            } else if (resourceContentIn.action == InAction.RemoveAll) {
                requireNormal()
                requireRemoveAll()
                actionList.clear()
                actionList.add(Pair(InAction.RemoveAll, mutableListOf()))
            } else if (resourceContentIn.action == InAction.Apply) {
                requireNormal()
                if (actionList.isEmpty() || actionList.last().first != InAction.Apply) {
                    actionList.add(Pair(InAction.Apply, mutableListOf(resourceContentIn.resourceContent!!)))
                } else {
                    actionList.last().second.add(resourceContentIn.resourceContent!!)
                }
            } else if (resourceContentIn.action == InAction.Remove) {
                requireNormal()
                if (actionList.isEmpty() || actionList.last().first != InAction.Remove) {
                    actionList.add(Pair(InAction.Remove, mutableListOf(resourceContentIn.resourceContent!!)))
                } else {
                    actionList.last().second.add(resourceContentIn.resourceContent!!)
                }
            } else if (resourceContentIn.action == InAction.Replace) {
                requireReplaceMode()
                replaceMode = true
                if (actionList.isEmpty()) {
                    actionList.add(Pair(InAction.Replace, mutableListOf(resourceContentIn.resourceContent!!)))
                } else {
                    actionList[0].second.add(resourceContentIn.resourceContent!!)
                }
            }
        }

        return actionList.map {
            ResultContentInGroup(it.first, it.second)
        }
    }
}