package org.tty.dailyset.contract.dao.async

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.contract.declare.ResourceTemporaryLink

/**
 * dao compat for [ResourceTemporaryLink]
 */
interface ResourceTemporaryLinkDaoCompatAsync<EC, TTL>: DaoCompatAsync<TTL> {
    fun findAllByUidAndTypeAndVersionNewerFlow(uid: String, type: EC, version: Int): Flow<List<TTL>>
    suspend fun findAllByUidAndTypeAndVersionNewer(uid: String, type: EC, version: Int): List<TTL>
    suspend fun applies(links: List<TTL>): Int
}