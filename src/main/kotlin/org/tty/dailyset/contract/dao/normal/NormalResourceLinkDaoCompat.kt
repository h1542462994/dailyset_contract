package org.tty.dailyset.contract.dao.normal

import org.tty.dailyset.contract.bean.declare.ResourceLink

/**
 * dao compat for [ResourceLink]
 */
interface NormalResourceLinkDaoCompat<EC, TL: ResourceLink<EC>> {
    fun findAllByUidAndTypeAndVersionNewer(uid: String, type: EC, version: Int): List<TL>
    fun applies(links: List<TL>): Int
}