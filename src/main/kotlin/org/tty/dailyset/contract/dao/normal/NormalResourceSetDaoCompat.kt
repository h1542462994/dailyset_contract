package org.tty.dailyset.contract.dao.normal

import org.tty.dailyset.contract.bean.declare.ResourceSet

/**
 * dao compat for [ResourceSet]
 */
interface NormalResourceSetDaoCompat<ES, TS: ResourceSet<ES>> {
    fun findByUid(uid: String): TS?
    fun findAllByUids(uids: List<String>): List<TS>
    fun apply(set: TS): Int
}