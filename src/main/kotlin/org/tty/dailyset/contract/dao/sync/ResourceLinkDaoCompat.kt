package org.tty.dailyset.contract.dao.sync

import org.tty.dailyset.contract.declare.ResourceLink

/**
 * dao compat for [ResourceLink]
 */
interface ResourceLinkDaoCompat<EC, TL: ResourceLink<EC>> {
    fun findAllByUidAndTypeAndVersionNewer(uid: String, type: EC, version: Int): List<TL>
    fun applies(links: List<TL>): Int
}