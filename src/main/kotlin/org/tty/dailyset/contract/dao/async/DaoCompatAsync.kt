package org.tty.dailyset.contract.dao.async

import kotlinx.coroutines.flow.Flow

/**
 * dao compat flow version.
 */
interface DaoCompatAsync<T> {
    /**
     * a cursor to support complex query.
     */
    fun cursor(): Flow<T?>
}