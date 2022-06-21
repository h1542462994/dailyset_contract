package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceDefaults
import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet
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
    @Suppress("UNCHECKED_CAST")
    private val setDescriptor = descriptorSetSync.setDescriptor as (ResourceSetDescriptorSync<Any, ES>)
    @Suppress("UNCHECKED_CAST")
    private val linkDescriptor = descriptorSetSync.linkDescriptor as (ResourceLinkDescriptorSync<Any, EC>)
    @Suppress("UNCHECKED_CAST")
    private val contentDescriptors = descriptorSetSync.contentDescriptors.map {
        it as ResourceContentDescriptorSync<out TC, Any, EC>
    }

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
            val converter = contentDescriptor.converter
            val linkedUids = links.filter { !it.isRemoved }.map { it.contentUid }
            val contents = contentDaoCompat.findAllByUids(linkedUids)
            contents.map {
                converter.convertFrom(it)
            }
        }
    }


    private fun applyContents(contentType: EC, contents: List<TC>) {
        val contentDescriptor = contentDescriptors.first { it.contentType == contentType }
        val contentDaoCompat = contentDescriptor.resourceContentDaoCompatSync
        val converter = contentDescriptor.converter

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

    fun contentTypes(): List<EC> {
        return contentDescriptors.map {
            it.contentType
        }
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
//        val type = content::class
//        val func = type.memberFunctions.find { it.name == "copy" }
//        requireNotNull(func)
//        val parameter = func.parameters.find { it.name == "uid" }
//        requireNotNull(parameter)
//        return func.callBy(mapOf(parameter to uid)) as TC

        return if (content.uid == uid) {
            content
        } else {
            content.copyByUid(uid) as TC
        }
    }
}