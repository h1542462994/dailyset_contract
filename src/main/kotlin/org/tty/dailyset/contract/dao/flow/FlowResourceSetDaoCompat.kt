package org.tty.dailyset.contract.dao.flow

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.contract.bean.declare.ResourceSet

/**
 * dao compat for [ResourceSet]
 */
interface FlowResourceSetDaoCompat<ES, TS: ResourceSet<ES>>: FlowDaoCompat<TS> {
    fun findByUidFlow(uid: String): Flow<TS?>
    fun findAllByUidsFlow(uids: List<String>): Flow<List<TS>>
    suspend fun findByUid(uid: String): TS?
    suspend fun findAllByUids(uids: List<String>): List<TS>
    suspend fun apply(set: TS): Int
}