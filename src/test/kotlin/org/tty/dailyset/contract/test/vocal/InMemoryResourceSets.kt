package org.tty.dailyset.contract.test.vocal

import org.tty.dailyset.contract.dao.sync.ResourceSetDaoCompatSync
import org.tty.dailyset.contract.declare.ResourceSet

class InMemoryResourceSets<ES>(
    private val internalStorage: MutableMap<String, ResourceSet<ES>> = mutableMapOf()
): MutableMap<String, ResourceSet<ES>> by internalStorage, ResourceSetDaoCompatSync<ResourceSet<ES>> {
    override fun findByUid(uid: String): ResourceSet<ES>? {
        return internalStorage[uid]
    }

    override fun findAllByUids(uids: List<String>): List<ResourceSet<ES>> {
        return uids.mapNotNull {
            internalStorage[it]
        }
    }

    override fun apply(set: ResourceSet<ES>): Int {
        internalStorage[set.uid] = set
        return 1
    }
}