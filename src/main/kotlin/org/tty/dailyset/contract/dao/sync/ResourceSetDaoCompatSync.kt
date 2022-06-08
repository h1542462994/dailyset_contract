package org.tty.dailyset.contract.dao.sync

import org.tty.dailyset.contract.declare.ResourceSet

/**
 * dao compat for [ResourceSet]
 */
interface ResourceSetDaoCompatSync<ES, TS: ResourceSet<ES>> {
    fun findByUid(uid: String): TS?
    fun findAllByUids(uids: List<String>): List<TS>
    fun apply(set: TS): Int
}