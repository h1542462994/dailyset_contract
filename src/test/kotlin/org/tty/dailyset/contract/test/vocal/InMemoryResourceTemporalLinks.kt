package org.tty.dailyset.contract.test.vocal

import org.tty.dailyset.contract.dao.sync.ResourceTemporalLinkDaoCompatSync
import org.tty.dailyset.contract.declare.LinkKey
import org.tty.dailyset.contract.declare.ResourceTemporalLink

class InMemoryResourceTemporalLinks<EC>(
    private val internalStorage: MutableMap<LinkKey<EC>, ResourceTemporalLink<EC>> = mutableMapOf()
): MutableMap<LinkKey<EC>, ResourceTemporalLink<EC>> by internalStorage, ResourceTemporalLinkDaoCompatSync<EC, ResourceTemporalLink<EC>> {
    override fun findAllByUidAndType(uid: String, type: EC): List<ResourceTemporalLink<EC>> {
        return internalStorage.values.filter {
            it.setUid == uid && it.contentType == type
        }

    }
    override fun applies(links: List<ResourceTemporalLink<EC>>): Int {
        links.forEach {
            internalStorage[it.key] = it
        }
        return links.size
    }

}