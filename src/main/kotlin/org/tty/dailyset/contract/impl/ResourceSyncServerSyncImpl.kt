package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.annotation.UseTransaction
import org.tty.dailyset.contract.dao.sync.TransactionSupportSync
import org.tty.dailyset.contract.dao.withTransactionR
import org.tty.dailyset.contract.data.*
import org.tty.dailyset.contract.declare.*
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
                internalWriteContents(set, it, timeWriting)
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
            internalWriteContents(set, typedResourcesApplying, timeWriting)
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
        return inTransaction {
            internalWriteTemporary(temporaryResult, timeWriting)
        }
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
        typedResourcesApplying: TypedResourcesApplying<TC, EC>,
        timeWriting: LocalDateTime,
    ) {
        val contentType = typedResourcesApplying.contentType

        typedResourcesApplying.prelude().forEach {
            descriptorDaoHelper.applyContentsServer(set, contentType, it.action, it.contents, timeWriting)
        }
    }

    private fun internalWriteTemporary(
        temporaryResult: TemporaryResult<TC, ES, EC>,
        timeWriting: LocalDateTime
    ): ResourceSet<ES> {
        val set = createIfAbsent(temporaryResult.set)
        val newTemporaryResult = temporaryResult.adjustTick(timeWriting)
        newTemporaryResult.typedResourcesTemp.forEach {
            internalWriteTemporaryContents(set, it)
        }
        return updateResourceSet(set)
    }

    private fun internalWriteTemporaryContents(
        set: ResourceSet<ES>,
        typedResourcesTemporary: TypedResourcesTemporary<TC, EC>,
    ) {
        val contentType = typedResourcesTemporary.contentType
        val links = descriptorDaoHelper.readLinks(set.uid, contentType)

        val resourceDiff2 = ResourceDiff2(
            sourceValues = links,
            targetValues = typedResourcesTemporary.resourceContentsTn,
            sourceKeySelector = { it.contentUid },
            targetKeySelector = { it.temporaryLink.contentUid }
        )

        val applyLinks = mutableListOf<ResourceLink<EC>>()
        val applyContents = mutableListOf<TC>()
        resourceDiff2.sameValues.map {
            val temporaryLink = it.targetValue.temporaryLink
            val content = it.targetValue.content
            if (temporaryLink.lastTick.isAfter(it.sourceValue.lastTick)) { // represents the link is newer
                applyLinks.add(
                    ResourceLink(
                        set.uid, contentType, temporaryLink.contentUid,
                        version = set.increasedVersion(),
                        isRemoved = temporaryLink.action == TemporaryAction.Remove,
                        lastTick = temporaryLink.lastTick
                    ),
                )
                if (content != null) {
                    applyContents.add(content)
                }
            }
        }
        resourceDiff2.addValues.map {
            val temporaryLink = it.temporaryLink
            val content = it.content
            applyLinks.add(
                ResourceLink(
                    set.uid, contentType, temporaryLink.contentUid,
                    version = set.increasedVersion(),
                    isRemoved = temporaryLink.action == TemporaryAction.Remove,
                    lastTick = temporaryLink.lastTick
                ),
            )
            if (content != null) {
                applyContents.add(content)
            }
        }

        descriptorDaoHelper.applyLinks(set.uid, contentType, applyLinks)
        descriptorDaoHelper.applyContents(contentType, applyContents)
    }
}