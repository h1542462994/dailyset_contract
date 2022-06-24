package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.data.InAction
import org.tty.dailyset.contract.dao.sync.ResourceContentDaoCompatSync
import org.tty.dailyset.contract.data.ResourceContentUn
import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*
import java.time.LocalDateTime
import java.util.*

/**
 * as SqliteHelper, to simplify the operation of dao interact.
 */
class DescriptorDaoHelperSync<TC: ResourceContent, ES, EC>(
    descriptorSetSync: DescriptorSetSync<TC, ES, EC>
) {

    // FIXME: unsafe typecast.
    @Suppress("UNCHECKED_CAST")
    private val setDescriptor = descriptorSetSync.setDescriptor as (ResourceSetDescriptorSync<Any, ES>)

    @Suppress("UNCHECKED_CAST")
    private val linkDescriptor = descriptorSetSync.linkDescriptor as (ResourceLinkDescriptorSync<Any, EC>)

    @Suppress("UNCHECKED_CAST")
    private val temporalLinkDescriptor = descriptorSetSync.temporalLinkDescriptor as (ResourceTemporalLinkDescriptor<Any, EC>)

    @Suppress("UNCHECKED_CAST")
    private val setVisibilityDescriptor = descriptorSetSync.setVisibilityDescriptor as (ResourceSetVisibilityDescriptorSync<Any>)
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
        return readLinksThen(uid, contentType, ResourceDefaults.VERSION_ZERO)
    }


    fun applyLinks(uid: String, contentType: EC, links: List<ResourceLink<EC>>): Int {
        val linkDaoCompat = linkDescriptor.resourceLinkDaoCompatSync
        val converter = linkDescriptor.converter
        links.forEach { require(it.setUid == uid && it.contentType == contentType) }

        return linkDaoCompat.applies(
            links.map { converter.convertTo(it) }
        )
    }

    fun readLinksThen(uid: String, contentType: EC, version: Int): List<ResourceLink<EC>> {
        val linkDaoCompat = linkDescriptor.resourceLinkDaoCompatSync
        val converter = linkDescriptor.converter
        val beans = linkDaoCompat.findAllByUidAndTypeAndVersionNewer(uid, contentType, version)
        return beans.map {
            converter.convertFrom(it)
        }
    }

    @Suppress("UNCHECKED_CAST", "DuplicatedCode")
    fun readContents(links: List<ResourceLink<EC>>, filter: (ResourceLink<EC>) -> Boolean): List<TC> {
        return if (links.isEmpty()) {
            emptyList()
        } else {
            val contentType = links.first().contentType
            val contentDescriptor = contentDescriptors.first { it.contentType == contentType }
            val contentDaoCompat = contentDescriptor.resourceContentDaoCompatSync
            val converter = contentDescriptor.converter as ResourceConverter<TC, Any>
            val linkedUids = links.filter { filter(it) }.map { it.contentUid }
            val contents = contentDaoCompat.findAllByUids(linkedUids)
            contents.map {
                converter.convertFrom(it)
            }
        }
    }

    @Suppress("UNCHECKED_CAST", "DuplicatedCode")
    fun readContentsUn(links: List<ResourceLink<EC>>, filter: (ResourceLink<EC>) -> Boolean): List<ResourceContentUn<TC, EC>> {
        return if (links.isEmpty()) {
            emptyList()
        } else {
            val contentType = links.first().contentType
            val contentDescriptor = contentDescriptors.first { it.contentType == contentType }
            val contentDaoCompat = contentDescriptor.resourceContentDaoCompatSync
            val converter = contentDescriptor.converter as ResourceConverter<TC, Any>
            val linkedUids = links.filter { filter(it) }.map { it.contentUid }
            val contents = contentDaoCompat.findAllByUids(linkedUids).map { converter.convertFrom(it) }.associateBy { it.uid }

            return links.map {
                ResourceContentUn(link = it, content = contents[it.contentUid]!!)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun applyContents(contentType: EC, contents: List<TC>) {
        val contentDescriptor = contentDescriptors.first { it.contentType == contentType }
        val contentDaoCompat = contentDescriptor.resourceContentDaoCompatSync as ResourceContentDaoCompatSync<Any>
        val converter = contentDescriptor.converter as ResourceConverter<TC, Any>

        contentDaoCompat.applies(contents.map { converter.convertTo(it) })
    }

    fun applyContentServer(set: ResourceSet<ES>, contentType: EC, action: InAction, contents: List<TC>, timeWriting: LocalDateTime) =
        when (action) {
            InAction.Single -> applyContentSingleServer(set, contentType, contents[0], timeWriting)
            InAction.RemoveAll -> applyContentRemoveAllServer(set, contentType, timeWriting)
            else -> applyContentUnionServer(set, contentType, action, contents, timeWriting)
        }

    private fun applyContentSingleServer(set: ResourceSet<ES>, contentType: EC, content: TC, timeWriting: LocalDateTime) {
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

    /**
     * used for [InAction.Apply], [InAction.Replace] and [InAction.Remove]
     */
    @Suppress("UNCHECKED_CAST")
    private fun applyContentUnionServer(set: ResourceSet<ES>, contentType: EC, action: InAction, contents: List<TC>, timeWriting: LocalDateTime) {
        require(action == InAction.Apply || action == InAction.Remove || action == InAction.Replace) {
            "other action is not supported."
        }

        val links = readLinks(set.uid, contentType)
        val existedContents = readContents(links) { true }
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

    private fun applyContentRemoveAllServer(set: ResourceSet<ES>, contentType: EC, timeWriting: LocalDateTime) {
        val links = readLinks(set.uid, contentType)
        applyLinks(set.uid, contentType, links.map { it.copy(version = set.increasedVersion(), isRemoved = true, lastTick = timeWriting) })
    }

    fun applyContentClient(set: ResourceSet<ES>, contentType: EC, action: InAction, contents: List<TC>, timeWriting: LocalDateTime) {
        when(action) {
            InAction.Single -> applyContentSingleClient(set, contentType, contents[0], timeWriting)
            InAction.RemoveAll -> applyContentRemoveAllClient(set, contentType, timeWriting)
            else -> applyContentUnionClient(set, contentType, action, contents, timeWriting)
        }
    }

    fun readSetVisibilities(userUid: String): List<ResourceSetVisibility> {
        val setVisibilityDaoCompatSync = setVisibilityDescriptor.resourceSetVisibilityDaoCompatSync
        val converter = setVisibilityDescriptor.converter

        return setVisibilityDaoCompatSync.findAllByUserUid(userUid).map { converter.convertFrom(it) }
    }

    fun applySetVisibilities(setVisibilities: List<ResourceSetVisibility>) {
        val setVisibilityDaoCompatSync = setVisibilityDescriptor.resourceSetVisibilityDaoCompatSync
        val converter = setVisibilityDescriptor.converter

        setVisibilityDaoCompatSync.applies(setVisibilities)
    }



    private fun applyContentSingleClient(set: ResourceSet<ES>, contentType: EC, content: TC, timeWriting: LocalDateTime) {

    }

    private fun applyContentUnionClient(set: ResourceSet<ES>, contentType: EC, action: InAction, contents: List<TC>, timeWriting: LocalDateTime) {

    }

    private fun applyContentRemoveAllClient(set: ResourceSet<ES>, contentType: EC, timeWriting: LocalDateTime) {

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