package org.tty.dailyset.contract.dao.async

import org.tty.dailyset.contract.dao.TransactionSupport

interface TransactionSupportAsync: TransactionSupport {
    fun withTransaction(func: suspend () -> Unit)
}