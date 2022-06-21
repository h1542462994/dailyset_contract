package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.bean.enums.InAction
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet

class ApplyingResultBuilder<out TC: ResourceContent, ES, EC> {
    private val store: MutableMap<EC, MutableList<ResourceContentIn< @UnsafeVariance TC>>> = mutableMapOf()

    fun remove(contentType: EC, content: @UnsafeVariance TC) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.add(ResourceContentIn(InAction.Remove, content))
    }

    fun removes(contentType: EC, contents: List<@UnsafeVariance TC>) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.addAll(contents.map { ResourceContentIn(InAction.Remove, it) })
    }

    fun removeAll(contentType: EC) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.add(ResourceContentIn(InAction.RemoveAll, null))
    }

    fun apply(contentType: EC, content: @UnsafeVariance TC) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.add(ResourceContentIn(InAction.Apply, content))
    }

    fun applies(contentType: EC, contents: List<@UnsafeVariance TC>) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.addAll(contents.map { ResourceContentIn(InAction.Apply, it) })
    }

    fun single(contentType: EC, content: @UnsafeVariance TC) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.add(ResourceContentIn(InAction.Single, content))
    }

    fun build(uid: String): ApplyingReq<TC, EC> {
        return ApplyingReq(
            uid,
            typedResourcesApplying = store.entries.map {
                TypedResourcesApplying(it.key, it.value)
            }
        )
    }
}

fun <TC: ResourceContent, ES, EC> applyingResult(
    uid: String,
    builderLambda: ApplyingResultBuilder<TC, ES, EC>.() -> Unit
): ApplyingReq<TC, EC> {
    val builder = ApplyingResultBuilder<TC, ES, EC>()
    builder.builderLambda()
    return builder.build(uid)
}