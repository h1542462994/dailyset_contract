package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.bean.enums.InAction
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet

class ApplyingResultBuilder<TS: ResourceSet<ES>, out TC: ResourceContent, ES, EC> {
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

    fun build(set: TS): ApplyingResult<TS, TC, ES, EC> {
        return ApplyingResult(
            set,
            typedResourcesApplying = store.entries.map {
                TypedResourcesApplying(it.key, it.value)
            }
        )
    }
}

fun <TS: ResourceSet<ES>, TC: ResourceContent, ES, EC> applyingResult(
    set: TS,
    builderLambda: ApplyingResultBuilder<TS, TC, ES, EC>.() -> Unit
): ApplyingResult<TS, TC, ES, EC> {
    val builder = ApplyingResultBuilder<TS, TC, ES, EC>()
    builder.builderLambda()
    return builder.build(set)
}