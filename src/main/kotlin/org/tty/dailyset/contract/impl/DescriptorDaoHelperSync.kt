package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.bean.enums.InAction
import org.tty.dailyset.contract.dao.sync.ResourceContentDaoCompatSync
import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.ResourceContentDescriptor
import org.tty.dailyset.contract.descriptor.ResourceContentDescriptorSync
import org.tty.dailyset.contract.descriptor.ResourceLinkDescriptorSync
import org.tty.dailyset.contract.descriptor.ResourceSetDescriptorSync
import java.time.LocalDateTime
import java.util.*

/**
 * as SqliteHelper, to simplify the operation of dao interact.
 */
class DescriptorDaoHelperSync<TC: ResourceContent, ES, EC>(
    private val descriptorSetSync: DescriptorSetSync<TC, ES, EC>
) {

    // FIXME: unsafe typecast.
    private val setDescriptor = descriptorSetSync.setDescriptor as (ResourceSetDescriptorSync<Any, ES>)
    private val linkDescriptor = descriptorSetSync.linkDescriptor as (ResourceLinkDescriptorSync<Any, EC>)
    private val contentDescriptors = descriptorSetSync.contentDescriptors

    fun readSet(uid: String): ResourceSet<ES>? {
        val setDaoCompat = setDescriptor.resourceSetDaoCompatSync
        val converter = setDescriptor.converter
        val bean = setDaoCompat.findByUid(uid)
        return if (bean == null) {
            null
        } else {
            converter.convertFrom(bean)
        }
    }

    fun readSets(uids: List<String>): List<ResourceSet<ES>> {
        val setDaoCompat = setDescriptor.resourceSetDaoCompatSync
        val converter = setDescriptor.converter
        val beans = setDaoCompat.findAllByUids(uids)
        return beans.map { converter.convertFrom(it) }
    }

    fun applySet(set: ResourceSet<ES>): Int {
        val setDaoCompat = setDescriptor.resourceSetDaoCompatSync
        val converter = setDescriptor.converter
        return setDaoCompat.apply(converter.convertTo(set))
    }

    fun readLinks(uid: String, contentType: EC): List<ResourceLink<EC>> {
        val linkDaoCompat = linkDescriptor.resourceLinkDaoCompatSync
        val converter = linkDescriptor.converter
        val beans = linkDaoCompat.findAllByUidAndTypeAndVersionNewer(uid, contentType, ResourceDefaults.VERSION_ZERO)
        return beans.map {
            converter.convertFrom(it)
        }
    }

    fun applyLinks(uid: String, contentType: EC, links: List<ResourceLink<EC>>): Int {
        val linkDaoCompat = linkDescriptor.resourceLinkDaoCompatSync
        val converter = linkDescriptor.converter
        links.forEach { require(it.setUid == uid && it.contentType == contentType) }

        return linkDaoCompat.applies(
            links.map { converter.convertTo(it) }
        )
    }

    fun readContents(links: List<ResourceLink<EC>>): List<TC> {
        return if (links.isEmpty()) {
            emptyList()
        } else {
            val contentType = links.first().contentType
            val contentDescriptor = contentDescriptors.first { it.contentType == contentType }
            val contentDaoCompat = contentDescriptor.resourceContentDaoCompatSync
            val converter = contentDescriptor.converter as ResourceConverter<TC, Any>
            val linkedUids = links.filter { !it.isRemoved }.map { it.contentUid }
            val contents = contentDaoCompat.findAllByUids(linkedUids)
            contents.map {
                converter.convertFrom(it)
            }
        }
    }


    private fun applyContents(contentType: EC, contents: List<TC>) {
        val contentDescriptor = contentDescriptors.first { it.contentType == contentType }
        val contentDaoCompat = contentDescriptor.resourceContentDaoCompatSync as ResourceContentDaoCompatSync<Any>
        val converter = contentDescriptor.converter as ResourceConverter<TC, Any>

        contentDaoCompat.applies(contents.map { converter.convertTo(it) })
    }


    fun applyContentSingle(set: ResourceSet<ES>, contentType: EC, content: TC, timeWriting: LocalDateTime) {
        val links = readLinks(set.uid, contentType)
        val newLink = if (links.isEmpty()) {
            ResourceLink(set.uid, contentType, assignedUid(content.uid),
                version = set.increasedVersion(),
                isRemoved = false,
                lastTick = timeWriting)
        } else {
            require(links.size == 1) { "single resource, but found more than 1 links." }
            val firstLink = links.first()
            require(firstLink.contentUid == content.uid || content.uid.isEmpty()) { "content uid is assigned, but it is differ from the link." }
            firstLink.copy(
                version = set.increasedVersion(),
                isRemoved = false,
                lastTick = timeWriting
            )
        }
        applyLinks(set.uid, contentType, listOf(newLink))
        applyContents(contentType, contents = listOf(dynamicCopyByUid(content, newLink.contentUid)))
    }

    @Suppress("UNCHECKED_CAST")
    fun applyContentUnion(set: ResourceSet<ES>, contentType: EC, contents: List<TC>, timeWriting: LocalDateTime, action: InAction) {
        require(action == InAction.Apply || action == InAction.Remove || action == InAction.Replace) {
            "other action is not supported."
        }

        val links = readLinks(set.uid, contentType)
        val existedContents = readContents(links)
        val contentDescriptor = contentDescriptor(contentType)

        val uidLessContents = contents.filter { it.uid.isEmpty() }
        val uidFulContents = contents.filter { it.uid.isNotEmpty() }

        val contentResourceDiff = ResourceDiff(
            sourceValues = existedContents,
            targetValues = uidLessContents,
            keySelector = contentDescriptor.keySelector as KeySelector<TC, Any>
        )

        val uidResourceDiff = ResourceDiff(
            sourceValues = existedContents,
            targetValues = uidFulContents,
            keySelector = ProvideKeySelector(func = { it.uid })
        )

        if (action == InAction.Apply || action == InAction.Replace) {
            // addition
            val contents1 = contentResourceDiff.addValues.map { it.copyByUid(assignedUid(it.uid)) }
            val contents2 = uidResourceDiff.addValues
            val addContents = contents1.plus(contents2)
            applyContents(contentType, contents2)
            applyLinks(set.uid, contentType, addContents.map { ResourceLink(set.uid, contentType, it.uid, set.increasedVersion(), false, timeWriting) })

            // same replace
            val targetValues = uidResourceDiff.sameValues.map { it.targetValue }
                .plus(contentResourceDiff.sameValues.map { it.targetValue.copyByUid(it.sourceValue.uid) as TC })
            applyContents(contentType, targetValues)
            applyLinks(set.uid, contentType, targetValues.map { ResourceLink(set.uid, contentType, it.uid, set.increasedVersion(), false, timeWriting) })
        }

        if (action == InAction.Replace) {
            // remove not occurred values
            val removes = contentResourceDiff.removeValues.intersect(uidResourceDiff.removeValues.toSet())
            applyLinks(set.uid, contentType, removes.map { ResourceLink(set.uid, contentType, it.uid, set.increasedVersion(), true, timeWriting) })
        }

        if (action == InAction.Remove) {
            // remove particular elements
            val uids = contentResourceDiff.removeValues.map { it.uid }
            val uids2 = uidResourceDiff.removeValues.map { it.uid }
            val removeUids = uids.plus(uids2)

            applyLinks(set.uid, contentType, removeUids.map { ResourceLink(set.uid, contentType, it, set.increasedVersion(), true, timeWriting) })
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun applyContentApply(set: ResourceSet<ES>, contentType: EC, contents: List<TC>, timeWriting: LocalDateTime) {
        applyContentUnion(set, contentType, contents, timeWriting, InAction.Apply)
    }

    fun applyContentReplace(set: ResourceSet<ES>, contentType: EC, contents: List<TC>, timeWriting: LocalDateTime) {
        applyContentUnion(set, contentType, contents, timeWriting, InAction.Apply)
    }

    fun applyContentRemove(set: ResourceSet<ES>, contentType: EC, contents: List<TC>, timeWriting: LocalDateTime) {
        applyContentUnion(set, contentType, contents, timeWriting, InAction.Remove)
    }

    fun applyContentRemoveAll(set: ResourceSet<ES>, contentType: EC, timeWriting: LocalDateTime) {
        TODO("not implemented yet.")
    }

    fun contentTypes(): List<EC> {
        return contentDescriptors.map {
            it.contentType
        }
    }

    private fun contentDescriptor(contentType: EC): ResourceContentDescriptorSync<out TC, *, EC> {
        return contentDescriptors.first { it.contentType == contentType }
    }

    /**
     * use provided uid, or generate a new one if provided uid is empty.
     */
    private fun assignedUid(uid: String): String {
        return uid.ifEmpty {
            UUID.randomUUID().toString()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun dynamicCopyByUid(content: TC, uid: String): TC {

        return if (content.uid == uid) {
            content
        } else {
            content.copyByUid(uid) as TC
        }
    }
}