package org.tty.dailyset.contract.test.vocal.memory

import org.tty.dailyset.contract.dao.sync.ResourceTemporaryLinkDaoCompatSync
import org.tty.dailyset.contract.declare.LinkKey
import org.tty.dailyset.contract.declare.ResourceTemporaryLink

class InMemoryResourceTemporaryLinks<EC>(
    private val internalStorage: MutableMap<LinkKey<EC>, ResourceTemporaryLink<EC>> = mutableMapOf()
): MutableMap<LinkKey<EC>, ResourceTemporaryLink<EC>> by internalStorage, ResourceTemporaryLinkDaoCompatSync<EC, ResourceTemporaryLink<EC>> {
    override fun findAllByUidAndType(uid: String, type: EC): List<ResourceTemporaryLink<EC>> {
        return internalStorage.values.filter {
            it.setUid == uid && it.contentType == type
        }

    }
    override fun applies(temporaryLinks: List<ResourceTemporaryLink<EC>>): Int {
        temporaryLinks.forEach {
            internalStorage[it.key] = it
        }
        return temporaryLinks.size
    }

    override fun countByUid(uid: String): Int {
        return internalStorage.values.count {
            it.setUid == uid
        }
    }

}