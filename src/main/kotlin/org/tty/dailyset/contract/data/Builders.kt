package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.bean.enums.InAction
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.Key

class ApplyReqBuilder<out TC: ResourceContent, EC> {
    private val store: MutableMap<EC, MutableList<ResourceContentIn<@UnsafeVariance TC>>> = mutableMapOf()

    /**
     * remove a resource.
     *
     * if **uid** is empty, it's determined by [Key], if is not empty, it's determined by **uid**.
     * @see Key
     */
    fun remove(contentType: EC, content: @UnsafeVariance TC) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.add(ResourceContentIn(InAction.Remove, content))
    }

    /**
     * remove resources.
     *
     * if **uid** is empty, it's determined by [Key], if is not empty, it's determined by **uid**.
     * @see Key
     */
    fun removes(contentType: EC, contents: List<@UnsafeVariance TC>) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.addAll(contents.map { ResourceContentIn(InAction.Remove, it) })
    }

    /**
     * remove allResources.
     *
     * **notice:** removeAll must be the first action (or not use) in each contentType.
     *
     * if **uid** is empty, it's determined by [Key], if is not empty, it's determined by **uid**.
     * @see Key
     */
    fun removeAll(contentType: EC) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.add(ResourceContentIn(InAction.RemoveAll, null))
    }

    /**
     * apply a resource.
     *
     * if **uid** is empty, it's determined by [Key], if is not empty, it's determined by **uid**.
     * @see Key
     */
    fun apply(contentType: EC, content: @UnsafeVariance TC) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.add(ResourceContentIn(InAction.Apply, content))
    }

    /**
     * apply resources.
     *
     * if **uid** is empty, it's determined by [Key], if is not empty, it's determined by **uid**.
     * @see Key
     */
    fun applies(contentType: EC, contents: List<@UnsafeVariance TC>) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.addAll(contents.map { ResourceContentIn(InAction.Apply, it) })
    }

    /**
     * replace a resources
     *
     * **notice:** if you use replace, other action **could not be used** in certain contentType. (Excludability)
     *
     * if **uid** is empty, it's determined by [Key], if is not empty, it's determined by **uid**.
     * @see Key
     */
    fun replace(contentType: EC, content: @UnsafeVariance TC) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.add(ResourceContentIn(InAction.Replace, content))
    }

    /**
     * replace resources.
     *
     * **notice:** if you use replace, other action **could not be used** in certain contentType. (Excludability)
     *
     * if **uid** is empty, it's determined by [Key], if is not empty, it's determined by **uid**.
     * @see Key
     */
    fun replaces(contentType: EC, contents: List<@UnsafeVariance TC>) {
        val list = store.getOrPut(contentType) { mutableListOf() }
        list.addAll(contents.map { ResourceContentIn(InAction.Replace, it) })
    }

    /**
     * change single resource.
     *
     * **notice:** if you use single, other action **could not be used** in certain contentType. (Excludability)
     *
     * it could only be called once.
     *
     * if **uid** is empty, it's determined by [Key], if is not empty, it's determined by **uid**.
     * @see Key
     */
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