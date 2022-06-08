package org.tty.dailyset.contract.dao.async

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.contract.declare.ResourceSet

/**
 * dao compat for [ResourceSet]
 */
interface ResourceSetDaoCompatAsync<TS>: DaoCompatAsync<TS> {
    fun findByUidFlow(uid: String): Flow<TS?>
    fun findAllByUidsFlow(uids: List<String>): Flow<List<TS>>
    suspend fun findByUid(uid: String): TS?
    suspend fun findAllByUids(uids: List<String>): List<TS>
    suspend fun apply(set: TS): Int
}