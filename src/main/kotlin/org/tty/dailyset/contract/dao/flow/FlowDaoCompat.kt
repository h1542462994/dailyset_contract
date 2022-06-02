package org.tty.dailyset.contract.dao.flow

import kotlinx.coroutines.flow.Flow

/**
 * dao compat flow version.
 */
interface FlowDaoCompat<T> {
    /**
     * a cursor to support complex query.
     */
    fun cursor(): Flow<T?>
}