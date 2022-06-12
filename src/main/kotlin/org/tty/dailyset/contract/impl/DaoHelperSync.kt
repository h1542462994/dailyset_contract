package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.dao.sync.ResourceContentDaoCompatSync
import org.tty.dailyset.contract.dao.sync.ResourceLinkDaoCompatSync
import org.tty.dailyset.contract.dao.sync.ResourceSetDaoCompatSync
import org.tty.dailyset.contract.declare.*

class DaoHelperSync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TC: ResourceContent, TV: ResourceSetVisibility, ES, EC>(
    private val descriptorSetSync: DescriptorSetSync<TS, TL, TTL, TC, TV, ES, EC>
) {
    private val setDescriptor = descriptorSetSync.setDescriptor
    private val linkDescriptor = descriptorSetSync.linkDescriptor
    private val contentDescriptors = descriptorSetSync.contentDescriptors

    @Suppress("UNCHECKED_CAST")
    fun readSet(uid: String): TS? {
        val setDaoCompat = setDescriptor.resourceSetDaoCompatSync as (ResourceSetDaoCompatSync<Any>)
        val converter = setDescriptor.converter as (ResourceConverter<TS, Any>)
        val bean = setDaoCompat.findByUid(uid)
        return if (bean == null) {
            null
        } else {
            converter.convertFrom(bean)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun readLinks(uid: String, contentType: EC): List<TL> {
        val linkDaoCompat = linkDescriptor.resourceLinkDaoCompatSync as (ResourceLinkDaoCompatSync<EC, Any>)
        val converter = linkDescriptor.converter as (ResourceConverter<TL, Any>)
        val beans = linkDaoCompat.findAllByUidAndTypeAndVersionNewer(uid, contentType, ResourceDefaults.VERSION_ZERO)
        return beans.map {
            converter.convertFrom(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun readContents(links: List<TL>): List<TC> {
        return if (links.isEmpty()) {
            emptyList()
        } else {
            val contentType = links.first().contentType
            val contentDescriptor = contentDescriptors.first { it.contentType == contentType }
            val contentDaoCompat = contentDescriptor.resourceContentDaoCompatSync as (ResourceContentDaoCompatSync<Any>)
            val converter = contentDescriptor.converter as (ResourceConverter<out TC, Any>)
            val linkedUids = links.filter { !it.isRemoved }.map { it.contentUid }
            val contents = contentDaoCompat.findAllByUids(linkedUids)
            contents.map {
                converter.convertFrom(it)
            }
        }
    }

    fun contentTypes(): List<EC> {
        return contentDescriptors.map {
            it.contentType
        }
    }
}