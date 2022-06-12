package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.dao.sync.TransactionSupportSync
import org.tty.dailyset.contract.data.*
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.module.sync.ResourceSyncServerSync

class ResourceSyncServerSyncImpl<TS : ResourceSet<ES>, TL : ResourceLink<EC>, TC : ResourceContent, ES, EC>(
    private val descriptorSet: DescriptorSetSync<TS, TL, *, TC, *, ES, EC>,
    private val transactionSupport: TransactionSupportSync?,
) : ResourceSyncServerSync<TS, TL, TC, ES, EC> {
    private val descriptorHelper = DaoHelperSync(descriptorSet)


    override fun readBase(uid: String): TS? {
        return descriptorHelper.readSet(uid)
    }

    override fun read(uid: String): SnapshotResult<TS, TC, ES, EC> {
        val set = requireNotNull(readBase(uid)) { "set(${uid} is not existed.)" }
        val typeResources = descriptorHelper.contentTypes()
            .map { contentType -> readContents(uid, contentType) }
            .filter { typedResources -> typedResources.resourceContents.isNotEmpty() }
        return SnapshotResult(
            set, typeResources
        )
    }

    override fun readContents(uid: String, contentType: EC): TypedResources<TC, EC> {
        require(readBase(uid) != null) { "set(${uid}) is not existed." }
        val links = descriptorHelper.readLinks(uid, contentType)
        val contents = descriptorHelper.readContents(links)
        return TypedResources(
            contentType = contentType,
            resourceContents = contents
        )
    }

    override fun write(result: ApplyingResult<TS, TC, ES, EC>): Relating<TS, ES> {
        TODO("Not yet implemented")
    }

    override fun writeContents(
        uid: String,
        contentType: EC,
        typedResourcesApplying: TypedResourcesApplying<TC, EC>
    ): Relating<TS, ES> {
        TODO("Not yet implemented")
    }

}