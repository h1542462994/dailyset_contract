package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.dao.sync.TransactionSupportSync
import org.tty.dailyset.contract.data.*
import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.module.sync.ResourceSyncClientSync
import java.time.LocalDateTime

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


    override fun createIfAbsent(set: ResourceSet<ES>): ResourceSet<ES> {
        TODO("Not yet implemented")
    }

    override fun write(req: ApplyingReq<TC, EC>, timeWriting: LocalDateTime): ResourceSet<ES> {
        TODO("Not yet implemented")
    }

    override fun writeContents(
        uid: String,
        typedResourcesApplying: TypedResourcesApplying<TC, EC>,
        timeWriting: LocalDateTime
    ): ResourceSet<ES> {
        TODO("Not yet implemented")
    }

    override fun writeUpdates(updateResult: UpdateResult<TC, ES, EC>): ResourceSet<ES> {
        TODO("Not yet implemented")
    }

    override fun writeUpdateContents(typedResourcesUpdate: TypedResourcesUpdate<TC, EC>): ResourceSet<ES> {
        TODO("Not yet implemented")
    }


}