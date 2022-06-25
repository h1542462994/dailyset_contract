package org.tty.dailyset.contract.dao.async

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.contract.declare.ResourceLink

/**
 * dao compat for [ResourceLink]
 * @param EC enum for resource content.
 * @param TL link storage data.
 */
interface ResourceLinkDaoCompatAsync<EC, TL>: DaoCompatAsync<TL> {
    fun findAllByUidAndTypeAndVersionNewerFlow(uid: String, type: EC, version: Int): Flow<List<TL>>
    suspend fun findAllByUidAndTypeAndVersionNewer(uid: String, type: EC, version: Int): List<TL>
    suspend fun applies(links: List<TL>): Int
}