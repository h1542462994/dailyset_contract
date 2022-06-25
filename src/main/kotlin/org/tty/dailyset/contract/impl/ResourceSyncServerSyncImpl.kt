package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.annotation.UseTransaction
import org.tty.dailyset.contract.dao.sync.TransactionSupportSync
import org.tty.dailyset.contract.dao.withTransactionR
import org.tty.dailyset.contract.data.*
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceDefaults
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.module.sync.ResourceSyncServerSync
import java.time.LocalDateTime

class ResourceSyncServerSyncImpl<TC : ResourceContent, ES, EC>(
    descriptorSet: DescriptorSetSync<TC, ES, EC>,
    private val transactionSupport: TransactionSupportSync?,
) : ResourceSyncServerSync<TC, ES, EC> {
    private val descriptorDaoHelper = DescriptorDaoHelperSync(descriptorSet)


    override fun readBase(uid: String): ResourceSet<ES>? {
        return descriptorDaoHelper.readSet(uid)
    }

    private fun requireReadBase(uid: String): ResourceSet<ES> {
        return requireNotNull(readBase(uid)) { "set(${uid} is not existed.)" }
    }

    override fun read(uid: String): SnapshotResult<TC, ES, EC> {
        val set = requireReadBase(uid)
        val typedResources = descriptorDaoHelper.contentTypes()
            .map { contentType -> internalReadContents(set, contentType) }
            .filter { typedResources -> typedResources.resourceContents.isNotEmpty() }
        return SnapshotResult(
            set, typedResources
        )
    }

    override fun readContents(uid: String, contentType: EC): TypedResources<TC, EC> {
        val set = requireReadBase(uid)
        return internalReadContents(set, contentType)
    }

    @Suppress("IfThenToElvis")
    override fun createIfAbsent(set: ResourceSet<ES>): ResourceSet<ES> {
        val existedSet = readBase(set.uid)
        return if (existedSet != null) {
            existedSet
        } else {
            inTransaction {
                val newSet = set.copy(version = ResourceDefaults.VERSION_INIT)
                descriptorDaoHelper.applySet(newSet)
                newSet
            }
        }
    }

    override fun write(
        req: ApplyingReq<TC, EC>,
        timeWriting: LocalDateTime,
    ): ResourceSet<ES> {
        val set = requireReadBase(req.setUid)
        return inTransaction {
            req.typedResourcesApplying.forEach {
                internalWriteContents(set, timeWriting, it)
            }
            updateResourceSet(set)
        }
    }

    @UseTransaction
    override fun writeContents(
        uid: String,
        typedResourcesApplying: TypedResourcesApplying<TC, EC>,
        timeWriting: LocalDateTime,
    ): ResourceSet<ES> {
        val set = requireReadBase(uid)
        return inTransaction {
            internalWriteContents(set, timeWriting, typedResourcesApplying)
            updateResourceSet(set)
        }
    }

    override fun readUpdateAll(sets: List<ResourceSet<ES>>): List<UpdateResult<TC, ES, EC>> {
        return sets.map {
            readUpdate(it.uid, it.version)
        }
    }

    override fun readUpdate(uid: String, version: Int): UpdateResult<TC, ES, EC> {
        val set = requireReadBase(uid)
        val typedResources = descriptorDaoHelper.contentTypes()
            .map { contentType -> internalReadUpdateContents(set, contentType, version) }
            .filter { typedResourcesUpdate -> typedResourcesUpdate.resourceContentsUn.isNotEmpty() }
        return UpdateResult(set, typedResources)
    }

    override fun readUpdateContents(uid: String, contentType: EC, version: Int): TypedResourcesUpdate<TC, EC> {
        val set = requireReadBase(uid)
        return internalReadUpdateContents(set, contentType, version)
    }

    override fun writeTemporaryAll(
        temporaryResults: List<TemporaryResult<TC, ES, EC>>,
        timeWriting: LocalDateTime
    ): List<ResourceSet<ES>> {
        return temporaryResults.map {
            writeTemporary(it, timeWriting)
        }
    }

    override fun writeTemporary(
        temporaryResult: TemporaryResult<TC, ES, EC>,
        timeWriting: LocalDateTime
    ): ResourceSet<ES> {
        TODO("Not yet implemented")
    }

    override fun writeTemporaryContents(
        uid: String,
        typedResourcesTemp: TypedResourcesTemporary<TC, EC>,
        timeWriting: LocalDateTime
    ): ResourceSet<ES> {
        TODO("Not yet implemented")
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R> inTransaction(action: () -> R): R {
        return transactionSupport.withTransactionR(action)
    }

    private fun updateResourceSet(set: ResourceSet<ES>): ResourceSet<ES> {
        val newSet = set.increaseVersionCopy()
        descriptorDaoHelper.applySet(newSet)
        return newSet
    }

    /**
     * internalReadContents for [read] and [readContents], the param is set.
     */
    private fun internalReadContents(
        set: ResourceSet<ES>,
        contentType: EC
    ): TypedResources<TC, EC> {
        val links = descriptorDaoHelper.readLinks(set.uid, contentType)
        val contents = descriptorDaoHelper.readContents(contentType, links) { !it.isRemoved }
        return TypedResources(
            contentType = contentType,
            resourceContents = contents
        )
    }

    /**
     * internalReadUpdateContents for [readUpdate]
     */
    private fun internalReadUpdateContents(
        set: ResourceSet<ES>,
        contentType: EC,
        version: Int
    ): TypedResourcesUpdate<TC, EC> {
        val links = descriptorDaoHelper.readLinksThen(set.uid, contentType, version)
        val contentsUn = descriptorDaoHelper.readContentsUn(contentType, links) { true }
        return TypedResourcesUpdate(
            contentType = contentType,
            resourceContentsUn = contentsUn
        )
    }

    /**
     * internalWriteContents for [write] and [writeContents], the param is set.
     */
    private fun internalWriteContents(
        set: ResourceSet<ES>,
        timeWriting: LocalDateTime,
        typedResourcesApplying: TypedResourcesApplying<TC, EC>,
    ) {
        val contentType = typedResourcesApplying.contentType

        typedResourcesApplying.prelude().forEach {
            descriptorDaoHelper.applyContentsServer(set, contentType, it.action, it.contents, timeWriting)
        }

    }

}