package org.tty.dailyset.contract.dao.normal

import org.tty.dailyset.contract.bean.declare.ResourceTemporalLink

/**
 * dao compat for [ResourceTemporalLink]
 */
interface NormalResourceTemporalLinkDaoCompat<EC, TTL: ResourceTemporalLink<EC>> {
    fun findAllByUidAndTypeAndVersionNewer(uid: String, type: EC, version: Int): List<TTL>
    fun applies(links: List<TTL>): Int
}