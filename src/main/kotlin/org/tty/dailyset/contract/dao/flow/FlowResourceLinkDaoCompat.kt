package org.tty.dailyset.contract.dao.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tty.dailyset.contract.bean.declare.ResourceLink

/**
 * dao compat for [ResourceLink]
 */
interface FlowResourceLinkDaoCompat<EC, TL: ResourceLink<EC>>: FlowDaoCompat<TL> {
    fun findAllByUidAndTypeAndVersionNewerFlow(uid: String, type: EC, version: Int): Flow<List<TL>>
    suspend fun findAllByUidAndTypeAndVersionNewer(uid: String, type: EC, version: Int): List<TL>
    suspend fun applies(links: List<TL>): Int
}