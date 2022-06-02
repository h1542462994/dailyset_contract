package org.tty.dailyset.contract.dao.flow

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.contract.bean.declare.ResourceTemporalLink

/**
 * dao compat for [ResourceTemporalLink]
 */
interface FlowResourceTemporalLinkDaoCompat<EC, TTL: ResourceTemporalLink<EC>>: FlowDaoCompat<TTL> {
    fun findAllByUidAndTypeAndVersionNewerFlow(uid: String, type: EC, version: Int): Flow<List<TTL>>
    suspend fun findAllByUidAndTypeAndVersionNewer(uid: String, type: EC, version: Int): List<TTL>
    suspend fun applies(links: List<TTL>): Int
}