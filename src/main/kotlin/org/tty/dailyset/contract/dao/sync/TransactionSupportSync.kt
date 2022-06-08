package org.tty.dailyset.contract.dao.sync

import org.tty.dailyset.contract.dao.TransactionSupport

interface TransactionSupportSync: TransactionSupport {
    fun withTransaction(func: () -> Unit)
}