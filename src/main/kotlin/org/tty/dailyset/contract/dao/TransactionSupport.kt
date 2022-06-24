package org.tty.dailyset.contract.dao

import org.tty.dailyset.contract.dao.sync.TransactionSupportSync

/**
 * provide transaction support.
 */
interface TransactionSupport

@Suppress("UNCHECKED_CAST")
fun <R> TransactionSupportSync?.withTransactionR(action: () -> R): R {
    var result: R ?= null
    if (this != null) {
        this.withTransaction { result = action() }
    } else {
        result = action()
    }
    return result as R
}