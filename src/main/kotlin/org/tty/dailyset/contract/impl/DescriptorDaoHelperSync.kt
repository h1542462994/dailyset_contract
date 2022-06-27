package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.dao.sync.*
import org.tty.dailyset.contract.data.InAction
import org.tty.dailyset.contract.data.ResourceContentTn
import org.tty.dailyset.contract.data.ResourceContentUn
import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*
import java.time.LocalDateTime
import java.util.*

/**
 * as SqliteHelper, to simplify the operation of dao interact.
 *
 * it defined some **atomic** operation to dao compat. converter is auto invoked.
 *
 * TODO: add comments.
 */
internal class DescriptorDaoHelperSync<TC: ResourceContent, ES, EC>(
    private val descriptorSetSync: DescriptorSetSync<TC, ES, EC>
) {

    /**
     * **atomic**
     */
    fun contentTypes(): List<EC> {
        return descriptorSetSync.contentDescriptors.map {
            it.contentType
        }
    }

    /**
     * **atomic**
     */
    fun readSet(uid: String): ResourceSet<ES>? {
        val (dao, converter) = setMeta()
        val bean = dao.findByUid(uid)
        return if (bean == null) {
            null
        } else {
            converter.convertFrom(bean)
        }
    }

    /**
     * **atomic**
     */
    fun readSets(uids: List<String>): List<ResourceSet<ES>> {
        val (dao, converter) = setMeta()
        val beans = dao.findAllByUids(uids)
        return beans.map { converter.convertFrom(it) }
    }

    /**
     * **atomic**
     */
    fun applySet(set: ResourceSet<ES>): Int {
        val (dao, converter) = setMeta()
        return dao.apply(converter.convertTo(set))
    }

    /**
     * **atomic**
     */
    fun readLinks(uid: String, contentType: EC): List<ResourceLink<EC>> {
        return readLinksThen(uid, contentType, ResourceDefaults.VERSION_ZERO)
    }

    /**
     * **atomic**
     */
    fun readLinksThen(uid: String, contentType: EC, version: Int): List<ResourceLink<EC>> {
        val (dao, converter) = linkMeta()
        val beans = dao.findAllByUidAndTypeAndVersionNewer(uid, contentType, version)
        return beans.map {
            converter.convertFrom(it)
        }
    }


    /**
     * **atomic**
     */
    fun applyLinks(uid: String, contentType: EC, links: List<ResourceLink<EC>>): Int {
        val (dao, converter) = linkMeta()
        links.forEach { require(it.setUid == uid && it.contentType == contentType) { "links contains wrong element." } }

        return dao.applies(
            links.map { converter.convertTo(it) }
        )
    }


    /**
     * **atomic**
     */
    fun readTemporaryLinkCount(uid: String): Int {
        val (dao, _) = temporaryLinkMeta()
        return dao.countByUid(uid)
    }

    /**
     * **atomic**
     */
    fun readTemporaryLinks(uid: String, contentType: EC): List<ResourceTemporaryLink<EC>> {
        val (dao, converter) = temporaryLinkMeta()
        return dao.findAllByUidAndType(uid, contentType).map { converter.convertFrom(it) }
    }

    /**
     * **atomic**
     */
    fun applyTemporaryLinks(uid: String, contentType: EC, temporaryLinks: List<ResourceTemporaryLink<EC>>) {
        val (dao, converter) = temporaryLinkMeta()
        temporaryLinks.forEach { require(it.setUid == uid && it.contentType == contentType) { "links contains wrong element." } }
        dao.applies(temporaryLinks)
    }


    /**
     * **atomic**
     */
    @Suppress("UNCHECKED_CAST")
    fun readContents(contentType: EC, links: List<ResourceLink<EC>>, filter: (ResourceLink<EC>) -> Boolean): List<TC> {
        val (dao, converter) = contentMetaOf(contentType)
        val linkedUids = links.filter { filter(it) }.map { it.contentUid }.ifEmpty { return listOf() }
        val contents = dao.findAllByUids(linkedUids)
        return contents.map {
            converter.convertFrom(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun readContentsClient(
        contentType: EC,
        links: List<ResourceLink<EC>>,
        temporaryLinks: List<ResourceTemporaryLink<EC>>
    ): List<TC> {
        val (dao, converter) = contentMetaOf(contentType)
        val localSuffixSupport = ResourceDefaults
        val linkedUids = links.filter { !it.isRemoved }.map { it.contentUid }

        val contents = if (linkedUids.isEmpty()) {
            emptyList()
        } else {
            val contents = dao.findAllByUids(linkedUids)
            contents.map { converter.convertFrom(it) }
        }

        val removedUids = temporaryLinks
            .filter { it.action == TemporaryAction.Remove }
            .map { it.contentUid }

        val temporaryLinkedUids = temporaryLinks.filter { it.action == TemporaryAction.Apply }
            .map { localSuffixSupport.addLocalSuffix(it.contentUid) }
        val temporaryContents = if (temporaryLinkedUids.isEmpty()) {
            emptyList()
        } else {
            val temporaryContents = dao.findAllByUids(temporaryLinkedUids)
            temporaryContents
                .map { converter.convertFrom(it) }
                .map { it.copyByUid(localSuffixSupport.removeLocalSuffix(it.uid)) as TC }
        }

        val resourceDiff = ResourceDiff(
            contents,
            temporaryContents,
            keySelector = { it.uid }
        )

        return resourceDiff.removeValues.filter { it.uid !in removedUids }
            .plus(resourceDiff.sameValues.map { it.targetValue })
            .plus(resourceDiff.addValues)
    }

    /**
     * **atomic**
     */
    @Suppress("UNCHECKED_CAST")
    fun readContentsUn(contentType: EC, links: List<ResourceLink<EC>>, filter: (ResourceLink<EC>) -> Boolean): List<ResourceContentUn<TC, EC>> {
        return if (links.isEmpty()) {
            emptyList()
        } else {
            val (dao, converter) = contentMetaOf(contentType)
            val linkedUids = links.filter { filter(it) }.map { it.contentUid }.ifEmpty { return emptyList() }
            val contents = dao.findAllByUids(linkedUids).map { converter.convertFrom(it) }.associateBy { it.uid }

            return links.map {
                ResourceContentUn(link = it, content = contents[it.contentUid]!!)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun readContentsTn(contentType: EC, temporaryLinks: List<ResourceTemporaryLink<EC>>, filter: (ResourceTemporaryLink<EC>) -> Boolean): List<ResourceContentTn<TC, EC>> {
        return if (temporaryLinks.isEmpty()) {
            emptyList()
        } else {
            val (dao, converter) = contentMetaOf(contentType)
            val localSuffixSupport: LocalSuffixSupport = ResourceDefaults

            val linkedUids = temporaryLinks.filter { filter(it) }.map { localSuffixSupport.addLocalSuffix(it.contentUid) }.ifEmpty { return emptyList() }
            val contents = dao.findAllByUids(linkedUids)
                .map { converter.convertFrom(it) }
                .map { it.copyByUid(localSuffixSupport.removeLocalSuffix(it.uid)) as TC }
                .associateBy { it.uid }

            return temporaryLinks.map {
                ResourceContentTn(temporaryLink = it, content = contents[it.contentUid])
            }
        }
    }

    /**
     * **atomic**
     */
    fun applyContents(contentType: EC, contents: List<TC>) {
        val (dao, converter) = contentMetaOf(contentType)
        dao.applies(contents.map { converter.convertTo(it) })
    }

    /**
     * **complex**
     */
    fun applyContentsServer(set: ResourceSet<ES>, contentType: EC, action: InAction, contents: List<TC>, timeWriting: LocalDateTime) =
        when (action) {
            InAction.Single -> applyContentsSingleServer(set, contentType, contents[0], timeWriting)
            InAction.RemoveAll -> applyContentsRemoveAllServer(set, contentType, timeWriting)
            else -> applyContentsUnionServer(set, contentType, action, contents, timeWriting)
        }

    private fun applyContentsSingleServer(set: ResourceSet<ES>, contentType: EC, content: TC, timeWriting: LocalDateTime) {
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
    @Suppress("UNCHECKED_CAST", "DuplicatedCode")
    private fun applyContentsUnionServer(set: ResourceSet<ES>, contentType: EC, action: InAction, contents: List<TC>, timeWriting: LocalDateTime) {
        require(action == InAction.Apply || action == InAction.Remove || action == InAction.Replace) {
            "other action is not supported."
        }

        if (contents.isEmpty()) {
            return
        }

        val links = readLinks(set.uid, contentType)
        val existedContents = readContents(contentType, links) { true }
        val contentDescriptor = contentDescriptor(contentType)


        val first = contents.first()
        // use first element to test it.
        val runResult = kotlin.runCatching { (contentDescriptor.keySelector as KeySelectorFunc<TC, Any>).invoke(first) }
        val supportsContentKey = runResult.isSuccess

        if (!supportsContentKey && action == InAction.Replace) {
            throw IllegalStateException("when use InAction.Replace mode, entry must implements Key<T> or provide KeySelectorFunc<T, TK>")
        }

        val resourceDiff = if (supportsContentKey) {
            ResourceDiff(
                sourceValues = existedContents,
                targetValues = contents,
                keySelector = contentDescriptor.keySelector as KeySelectorFunc<TC, Any>
            )
        } else {
            ResourceDiff(
                sourceValues = existedContents,
                targetValues = contents,
                keySelector = { it.uid }
            )
        }

        fun toLinks(contents: Iterable<TC>, isRemoved: Boolean): List<ResourceLink<EC>> {
            return contents.map {
                ResourceLink(set.uid, contentType, it.uid, set.increasedVersion(), isRemoved, timeWriting)
            }
        }

        if (action == InAction.Apply || action == InAction.Replace) {
            // addition
            val addContents = resourceDiff.addValues
                .map { it.copyByUid(assignedUid(it.uid)) as TC }
            applyContents(contentType, addContents)
            applyLinks(set.uid, contentType, toLinks(addContents, false))

            // same replace
            val sameContents = resourceDiff.sameValues.map { it.targetValue.copyByUid(it.sourceValue.uid) as TC }
            applyContents(contentType, sameContents)
            applyLinks(set.uid, contentType, toLinks(sameContents, false))
        }

        if (action == InAction.Replace) {
            // remove not occurred values
            val removes = resourceDiff.removeValues
            applyLinks(set.uid, contentType, toLinks(removes, true))
        }

        if (action == InAction.Remove) {
            // remove particular elements
            val removes = resourceDiff.addValues
            applyLinks(set.uid, contentType, toLinks(removes, true))
        }
    }

    private fun applyContentsRemoveAllServer(set: ResourceSet<ES>, contentType: EC, timeWriting: LocalDateTime) {
        val links = readLinks(set.uid, contentType)
        applyLinks(set.uid, contentType, links.map { it.copy(version = set.increasedVersion(), isRemoved = true, lastTick = timeWriting) })
    }

    /**
     * **complex**
     */
    fun applyContentsClient(set: ResourceSet<ES>, contentType: EC, action: InAction, contents: List<TC>, timeWriting: LocalDateTime) {
        when(action) {
            InAction.Single -> applyContentSingleClient(set, contentType, contents[0], timeWriting)
            InAction.RemoveAll -> applyContentRemoveAllClient(set, contentType, timeWriting)
            else -> applyContentUnionClient(set, contentType, action, contents, timeWriting)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun applyContentSingleClient(set: ResourceSet<ES>, contentType: EC, content: TC, timeWriting: LocalDateTime) {
        val links = readLinks(set.uid, contentType)
        require(links.isEmpty() || links.size == 1) { "single resource, but found more than 1 links." }
        if (links.isNotEmpty()) {
            require(links[0].contentUid == content.uid || content.uid.isEmpty()) { "content uid is assigned, but it is differ from the link." }
        }

        val localSuffixSupport = ResourceDefaults
        val addContent = content.copyByUid(localSuffixSupport.addLocalSuffix(assignedUid(content.uid))) as TC
        val temporaryLink = ResourceTemporaryLink(
            set.uid,
            contentType,
            localSuffixSupport.removeLocalSuffix(addContent.uid),
            action = TemporaryAction.Apply,
            state = TemporaryState.Created,
            lastTick = timeWriting
        )

        applyContents(contentType, contents = listOf(addContent))
        applyTemporaryLinks(set.uid, contentType, listOf(temporaryLink))
    }

    @Suppress("UNCHECKED_CAST", "DuplicatedCode")
    private fun applyContentUnionClient(set: ResourceSet<ES>, contentType: EC, action: InAction, contents: List<TC>, timeWriting: LocalDateTime) {
        require(action == InAction.Apply || action == InAction.Remove || action == InAction.Replace) {
            "other action is not supported."
        }

        val links = readLinks(set.uid, contentType)
        val existedContents = readContents(contentType, links) { true }
        val contentDescriptor = contentDescriptor(contentType)

        val uidLessContents = contents.filter { it.uid.isEmpty() }
        val uidFulContents = contents.filter { it.uid.isNotEmpty() }

        val contentResourceDiff = ResourceDiff(
            sourceValues = existedContents,
            targetValues = uidLessContents,
            keySelector = contentDescriptor.keySelector as KeySelectorFunc<TC, Any>
        )

        val uidResourceDiff = ResourceDiff(
            sourceValues = existedContents,
            targetValues = uidFulContents,
            keySelector = { it.uid }
        )

        val localSuffixSupport = ResourceDefaults

        fun toTemporaryLinks(contents: Iterable<TC>, isRemoved: Boolean, addSuffix: Boolean): List<ResourceTemporaryLink<EC>> {
            return contents.map {
                ResourceTemporaryLink(set.uid, contentType,
                    contentUid = if (addSuffix) { localSuffixSupport.removeLocalSuffix(it.uid) } else it.uid,
                    action = if (isRemoved) TemporaryAction.Remove else TemporaryAction.Apply,
                    state = TemporaryState.Created,
                    lastTick = timeWriting
                )
            }
        }

        if (action == InAction.Apply || action == InAction.Replace) {
            // addition
            val addContents = contentResourceDiff.addValues
                .map { it.copyByUid(localSuffixSupport.addLocalSuffix(assignedUid(it.uid))) as TC }
                .plus(uidResourceDiff.addValues.map { it.copyByUid(localSuffixSupport.addLocalSuffix(it.uid)) as TC })
            applyContents(contentType, addContents)
            applyTemporaryLinks(set.uid, contentType, toTemporaryLinks(addContents, isRemoved = false, addSuffix = true))

            // same replace
            val sameContents = uidResourceDiff.sameValues
                .map { it.targetValue.copyByUid(localSuffixSupport.addLocalSuffix(it.sourceValue.uid)) as TC }
                .plus(contentResourceDiff.sameValues.map { it.targetValue.copyByUid(localSuffixSupport.addLocalSuffix(it.sourceValue.uid)) as TC })
            applyContents(contentType, sameContents)
            applyTemporaryLinks(set.uid, contentType, toTemporaryLinks(sameContents, isRemoved = false, addSuffix = true))
        }

        if (action == InAction.Replace) {
            // remove not occurred values
            val removes = contentResourceDiff.removeValues.intersect(uidResourceDiff.removeValues.toSet())
            applyTemporaryLinks(set.uid, contentType, toTemporaryLinks(removes, isRemoved = true, addSuffix = false))
        }

        if (action == InAction.Remove) {
            // remove particular elements
            val removes = uidResourceDiff.addValues
                .plus(contentResourceDiff.addValues)

            applyTemporaryLinks(set.uid, contentType, toTemporaryLinks(removes, isRemoved = true, addSuffix = false))
        }
    }

    private fun applyContentRemoveAllClient(set: ResourceSet<ES>, contentType: EC, timeWriting: LocalDateTime) {
        val links = readLinks(set.uid, contentType)
        applyTemporaryLinks(set.uid, contentType, links.map {
            ResourceTemporaryLink(set.uid, contentType, it.contentUid,
                action = TemporaryAction.Remove,
                state = TemporaryState.Created,
                lastTick = timeWriting)
        })
    }

    fun readSetVisibilities(userUid: String): List<ResourceSetVisibility> {
        val (dao, converter) = setVisibilityMeta()
        return dao.findAllByUserUid(userUid).map { converter.convertFrom(it) }
    }

    fun applySetVisibilities(setVisibilities: List<ResourceSetVisibility>) {
        val (dao, converter) = setVisibilityMeta()
        dao.applies(setVisibilities.map { converter.convertTo(it) })
    }


    @Suppress("UNCHECKED_CAST")
    private fun contentDescriptor(contentType: EC): ResourceContentDescriptorSync<out TC, *, EC> {
        return descriptorSetSync.contentDescriptors.first { it.contentType == contentType }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setMeta(): Pair<ResourceSetDaoCompatSync<Any>, ResourceConverter<ResourceSet<ES>, Any>> {
        val setDescriptor = descriptorSetSync.setDescriptor as ResourceSetDescriptorSync<Any, ES>
        val setDaoCompat = setDescriptor.resourceSetDaoCompatSync
        val converter = setDescriptor.converter
        return Pair(setDaoCompat, converter)
    }

    @Suppress("UNCHECKED_CAST")
    private fun linkMeta(): Pair<ResourceLinkDaoCompatSync<EC, Any>, ResourceConverter<ResourceLink<EC>, Any>> {
        val linkDescriptor = descriptorSetSync.linkDescriptor as ResourceLinkDescriptorSync<Any, EC>
        val linkDaoCompat = linkDescriptor.resourceLinkDaoCompatSync
        val converter = linkDescriptor.converter
        return Pair(linkDaoCompat, converter)
    }

    @Suppress("UNCHECKED_CAST")
    private fun temporaryLinkMeta(): Pair<ResourceTemporaryLinkDaoCompatSync<EC, Any>, ResourceConverter<ResourceTemporaryLink<EC>, Any>> {
        val temporaryLinkDescriptor = descriptorSetSync.temporaryLinkDescriptor as ResourceTemporaryLinkDescriptorSync<Any, EC>
        val temporaryLinkDaoCompat = temporaryLinkDescriptor.resourceTemporaryLinkDaoCompatSync
        val converter = temporaryLinkDescriptor.converter
        return Pair(temporaryLinkDaoCompat, converter)
    }

    @Suppress("UNCHECKED_CAST")
    private fun contentMetaOf(contentType: EC): Pair<ResourceContentDaoCompatSync<Any>, ResourceConverter<TC, Any>> {
        val contentDescriptor = contentDescriptor(contentType)
        val contentDaoCompat = contentDescriptor.resourceContentDaoCompatSync as ResourceContentDaoCompatSync<Any>
        val converter = contentDescriptor.converter as ResourceConverter<TC, Any>
        return Pair(contentDaoCompat, converter)
    }


    @Suppress("UNCHECKED_CAST")
    private fun setVisibilityMeta(): Pair<ResourceSetVisibilityDaoCompatSync<Any>, ResourceConverter<ResourceSetVisibility, Any>> {
        val setVisibilityDescriptor = descriptorSetSync.setVisibilityDescriptor as ResourceSetVisibilityDescriptorSync<Any>
        val setVisibilityDaoCompat = setVisibilityDescriptor.resourceSetVisibilityDaoCompatSync
        val converter = setVisibilityDescriptor.converter
        return Pair(setVisibilityDaoCompat, converter)
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