package org.tty.dailyset.contract.dao.async

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.contract.declare.ResourceContent

/**
 * compat dao for [ResourceContent]
 * @param TC the storage data.
 */
interface ResourceContentDaoCompatAsync<TC>: DaoCompatAsync<TC> {
    fun findAllByUidsFlow(uids: List<String>): Flow<List<TC>>
    suspend fun findAllByUids(uids: List<String>): List<TC>
    suspend fun applies(contents: List<TC>): Int
}