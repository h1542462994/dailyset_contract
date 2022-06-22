package org.tty.dailyset.contract.dao.sync

import org.tty.dailyset.contract.declare.ResourceTemporalLink

/**
 * dao compat for [ResourceTemporalLink]
 */
interface ResourceTemporalLinkDaoCompatSync<EC, TTL> {
    fun findAllByUidAndType(uid: String, type: EC): List<TTL>
    fun applies(links: List<TTL>): Int
}