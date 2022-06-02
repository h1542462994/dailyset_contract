package org.tty.dailyset.contract.dao.flow

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.contract.bean.declare.ResourceContent

/**
 * compat dao for [ResourceContent]
 */
interface FlowResourceContentDaoCompat<TC: ResourceContent>: FlowDaoCompat<TC> {
    fun findAllByUidsFlow(uids: List<String>): Flow<List<TC>>
    suspend fun findAllByUids(uids: List<String>): List<TC>
    suspend fun applies(contents: List<TC>): Int
}