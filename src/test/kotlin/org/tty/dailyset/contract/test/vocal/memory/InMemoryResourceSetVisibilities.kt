package org.tty.dailyset.contract.test.vocal.memory

import org.tty.dailyset.contract.dao.sync.ResourceSetVisibilityDaoCompatSync
import org.tty.dailyset.contract.declare.ResourceSetVisibility

class InMemoryResourceSetVisibilities(
    private val internalStorage: MutableMap<String, ResourceSetVisibility> = mutableMapOf()
): MutableMap<String, ResourceSetVisibility> by internalStorage, ResourceSetVisibilityDaoCompatSync<ResourceSetVisibility> {

    override fun applies(visibilities: List<ResourceSetVisibility>): Int {
        visibilities.forEach {
            internalStorage[it.uid] = it
        }
        return visibilities.size
    }

    override fun findAllByUserUid(userUid: String): List<ResourceSetVisibility> {
        return internalStorage.values.filter {
            it.userUid == userUid
        }
    }
}