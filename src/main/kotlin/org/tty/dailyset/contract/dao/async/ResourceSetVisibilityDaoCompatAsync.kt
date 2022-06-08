package org.tty.dailyset.contract.dao.async

import kotlinx.coroutines.flow.Flow

interface ResourceSetVisibilityDaoCompatAsync<TV>: DaoCompatAsync<TV> {
    fun findAllByUserUidFlow(userUid: String): Flow<List<TV>>
    suspend fun findAllByUserUid(userUid: String): List<TV>
    fun applies(visibilities: List<TV>): Int
}