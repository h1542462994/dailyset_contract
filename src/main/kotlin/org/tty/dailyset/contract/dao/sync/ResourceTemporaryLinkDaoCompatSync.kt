package org.tty.dailyset.contract.dao.sync

import org.tty.dailyset.contract.declare.ResourceTemporaryLink

/**
 * dao compat for [ResourceTemporaryLink]
 */
interface ResourceTemporaryLinkDaoCompatSync<EC, TTL> {
    fun findAllByUidAndType(uid: String, type: EC): List<TTL>
    fun applies(temporaryLinks: List<TTL>): Int
    fun countByUid(uid: String): Int
}