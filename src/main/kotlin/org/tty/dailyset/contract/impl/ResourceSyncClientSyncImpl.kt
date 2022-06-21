package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.dao.sync.TransactionSupportSync
import org.tty.dailyset.contract.data.SnapshotResult
import org.tty.dailyset.contract.data.TypedResources
import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.module.sync.ResourceSyncClientSync

class ResourceSyncClientSyncImpl<TC : ResourceContent, ES, EC>(
    private val descriptorSet: DescriptorSetSync<TC, ES, EC>,
    private val transactionSupport: TransactionSupportSync?
) : ResourceSyncClientSync<TC, ES, EC> {

    override fun readBase(uid: String): ResourceSet<ES>? {
        TODO("Not yet implemented")
    }
    override fun read(uid: String): SnapshotResult<TC, ES, EC> {
        TODO("Not yet implemented")
    }

    override fun readContents(uid: String, contentType: EC): TypedResources<TC, EC> {
        TODO("Not yet implemented")
    }

}