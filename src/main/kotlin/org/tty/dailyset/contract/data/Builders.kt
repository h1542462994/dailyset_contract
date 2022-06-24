package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.bean.enums.InAction
import org.tty.dailyset.contract.declare.ResourceContent

class ApplyReqBuilder<out TC: ResourceContent, EC> {
    private val store: MutableMap<EC, MutableList<ResourceContentIn<@UnsafeVariance TC>>> = mutableMapOf()

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

    fun replace(contentType: EC, content: @UnsafeVariance TC) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.add(ResourceContentIn(InAction.Replace, content))
    }

    fun replaces(contentType: EC, contents: List<@UnsafeVariance TC>) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.addAll(contents.map { ResourceContentIn(InAction.Replace, it) })
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

fun <TC: ResourceContent, EC> applyingReq(
    uid: String,
    builderLambda: ApplyReqBuilder<TC, EC>.() -> Unit
): ApplyingReq<TC, EC> {
    val builder = ApplyReqBuilder<TC, EC>()
    builder.builderLambda()
    return builder.build(uid)
}