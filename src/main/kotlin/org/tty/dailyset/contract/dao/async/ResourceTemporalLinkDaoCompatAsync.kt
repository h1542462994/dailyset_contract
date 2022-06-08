package org.tty.dailyset.contract.dao.async

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.contract.declare.ResourceTemporalLink

/**
 * dao compat for [ResourceTemporalLink]
 */
interface ResourceTemporalLinkDaoCompatAsync<EC, TTL: ResourceTemporalLink<EC>>: DaoCompatAsync<TTL> {
    fun findAllByUidAndTypeAndVersionNewerFlow(uid: String, type: EC, version: Int): Flow<List<TTL>>
    suspend fun findAllByUidAndTypeAndVersionNewer(uid: String, type: EC, version: Int): List<TTL>
    suspend fun applies(links: List<TTL>): Int
}