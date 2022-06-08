package org.tty.dailyset.contract.dao.sync

import org.tty.dailyset.contract.declare.ResourceTemporalLink

/**
 * dao compat for [ResourceTemporalLink]
 */
interface ResourceTemporalLinkDaoCompatSync<EC, TTL: ResourceTemporalLink<EC>> {
    fun findAllByUidAndTypeAndVersionNewer(uid: String, type: EC, version: Int): List<TTL>
    fun applies(links: List<TTL>): Int
}