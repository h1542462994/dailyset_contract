package org.tty.dailyset.contract.test.vocal

import org.tty.dailyset.contract.dao.sync.ResourceLinkDaoCompatSync
import org.tty.dailyset.contract.declare.LinkKey
import org.tty.dailyset.contract.declare.ResourceLink

class InMemoryResourceLinks<EC>(
    private val internalStorage: MutableMap<LinkKey<EC>, ResourceLink<EC>> = mutableMapOf()
): MutableMap<LinkKey<EC>, ResourceLink<EC>> by internalStorage, ResourceLinkDaoCompatSync<EC, ResourceLink<EC>>  {
    override fun findAllByUidAndTypeAndVersionNewer(uid: String, type: EC, version: Int): List<ResourceLink<EC>> {
        return internalStorage.values.filter {
            it.setUid == uid && it.contentType == type && it.version >= version
        }
    }

    override fun applies(links: List<ResourceLink<EC>>): Int {
        links.forEach {
            internalStorage[it.key] = it
        }
        return links.size
    }

}