package org.tty.dailyset.contract.test.vocal

import org.tty.dailyset.contract.dao.sync.ResourceContentDaoCompatSync
import org.tty.dailyset.contract.declare.ResourceContent

class InMemoryResourceContents<T: ResourceContent>(
    private val internalStorage: MutableMap<String, T> = mutableMapOf()
): MutableMap<String, T> by internalStorage, ResourceContentDaoCompatSync<T> {
    override fun applies(contents: List<T>): Int {
        contents.forEach {
            internalStorage[it.uid] = it
        }
        return contents.size
    }

    override fun findAllByUids(uids: List<String>): List<T> {
        return uids.mapNotNull {
            internalStorage[it]
        }
    }
}