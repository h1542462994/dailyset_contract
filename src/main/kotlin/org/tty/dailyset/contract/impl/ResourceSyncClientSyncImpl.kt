package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.dao.sync.TransactionSupportSync
import org.tty.dailyset.contract.dao.withTransactionR
import org.tty.dailyset.contract.data.*
import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.module.sync.ResourceSyncClientSync
import java.time.LocalDateTime

class ResourceSyncClientSyncImpl<TC : ResourceContent, ES, EC>(
    descriptorSet: DescriptorSetSync<TC, ES, EC>,
    private val transactionSupport: TransactionSupportSync?
) : ResourceSyncClientSync<TC, ES, EC> {
    private val descriptorDaoHelper = DescriptorDaoHelperSync(descriptorSet)

    override var userContext: UserContext = UserContext.EMPTY

    override fun readBase(uid: String): ResourceSet<ES>? {
        return descriptorDaoHelper.readSet(uid)
    }

    override fun read(uid: String): SnapshotResult<TC, ES, EC> {
        val set = requireReadBase(uid)
        val typeResources = descriptorDaoHelper.contentTypes()
            .map { contentType -> internalReadContents(set, contentType) }
            .filter { typedResources -> typedResources.resourceContents.isNotEmpty() }
        return SnapshotResult(
            set, typeResources
        )
    }

    override fun readContents(uid: String, contentType: EC): TypedResources<TC, EC> {
        val set = requireReadBase(uid)
        return internalReadContents(set, contentType)
    }


    override fun createIfAbsent(set: ResourceSet<ES>): ResourceSet<ES> {
        val existSet = readBase(set.uid)
        return if (existSet != null) {
            existSet
        } else {
            val newSet = set.copy(version = ResourceDefaults.VERSION_ZERO)
            descriptorDaoHelper.applySet(newSet)
            newSet
        }
    }

    override fun write(req: ApplyingReq<TC, EC>, timeWriting: LocalDateTime): ResourceSet<ES> {
        val set = requireReadBase(req.setUid)
        return inTransaction {
            req.typedResourcesApplying.forEach {
                internalWriteContents(set, it, timeWriting)
            }
            // NOTICE: client has no ability to manage version.
            set
        }
    }

    override fun writeContents(
        uid: String,
        typedResourcesApplying: TypedResourcesApplying<TC, EC>,
        timeWriting: LocalDateTime
    ): ResourceSet<ES> {
        val set = requireReadBase(uid)
        return inTransaction {
            internalWriteContents(set, typedResourcesApplying, timeWriting)
            // NOTICE: client has no ability to manage version.
            set
        }
    }

    override fun writeUpdateAll(updateResults: List<UpdateResult<TC, ES, EC>>): List<ResourceSet<ES>> {
        return inTransaction {
            updateResults.map {
                internalWriteUpdate(it)
            }
        }
    }

    override fun writeUpdate(updateResult: UpdateResult<TC, ES, EC>): ResourceSet<ES> {
        return inTransaction {
            internalWriteUpdate(updateResult)
        }
    }

    override fun readTemporaryAll(timeReading: LocalDateTime): List<TemporaryResult<TC, ES, EC>> {
        val sets = readTemporaryBases()
        return readTemporaryAll(sets.map { it.uid }, timeReading)
    }

    override fun readTemporaryAll(uids: List<String>, timeReading: LocalDateTime): List<TemporaryResult<TC, ES, EC>> {
        return inTransaction {
            uids.map {
                internalReadTemporary(it, timeReading)
            }
        }
    }

    override fun readTemporary(uid: String, timeReading: LocalDateTime): TemporaryResult<TC, ES, EC> {
        return inTransaction {
            internalReadTemporary(uid, timeReading)
        }
    }

    override fun acceptTemporaryAll() {
        val sets = readAvailableBases()
        acceptTemporaryAll(sets.map { it.uid })
    }


    override fun acceptTemporaryAll(uids: List<String>) {
        inTransaction {
            uids.forEach {
                internalAcceptTemporary(it)
            }
        }
    }

    override fun acceptTemporary(uid: String) {
        inTransaction {
            internalAcceptTemporary(uid)
        }
    }



    override fun readAvailableBases(): List<ResourceSet<ES>> {
        val userContext = requireUserContext()
        val uids = descriptorDaoHelper.readSetVisibilities(userContext.userUid)
            .filter { it.visible }
            .map { it.uid }
        return descriptorDaoHelper.readSets(uids)
    }

    override fun writeAvailableBases(sets: List<ResourceSet<ES>>) {
        val userContext = requireUserContext()
        sets.forEach {
            descriptorDaoHelper.applySet(it)
        }
        val setVisibilities = descriptorDaoHelper.readSetVisibilities(userContext.userUid)
        val diff = ResourceDiff2(
            setVisibilities,
            sets,
            sourceKeySelector = { it.uid },
            targetKeySelector = { it.uid }
        )

        val newVisibilities = mutableListOf<ResourceSetVisibility>()
        newVisibilities.addAll(diff.removeValues.map { it.copy(visible = false) })
        newVisibilities.addAll(diff.sameValues.map { it.sourceValue.copy(visible = true) })
        newVisibilities.addAll(diff.addValues.map { ResourceSetVisibility(uid = it.uid, userUid = userContext.userUid, visible = true) })
        descriptorDaoHelper.applySetVisibilities(newVisibilities)
    }

    override fun readTemporaryBases(): List<ResourceSet<ES>> {
        val sets = readAvailableBases()
        return sets.filter {
            setHasTemporary(it)
        }
    }

    private fun requireReadBase(uid: String): ResourceSet<ES> {
        return requireNotNull(readBase(uid)) { "set(${uid} is not existed.)" }
    }

    private fun setHasTemporary(resourceSet: ResourceSet<ES>): Boolean {
        return (resourceSet.version == ResourceDefaults.VERSION_ZERO || descriptorDaoHelper.readTemporaryLinkCount(resourceSet.uid) > 0)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R> inTransaction(action: () -> R): R {
        return transactionSupport.withTransactionR(action)
    }

    private fun internalReadContents(set: ResourceSet<ES>, contentType: EC): TypedResources<TC, EC> {
        val links = descriptorDaoHelper.readLinks(set.uid, contentType)
        val temporaryLinks = descriptorDaoHelper.readTemporaryLinks(set.uid, contentType)
        val contents = descriptorDaoHelper.readContentsClient(contentType, links, temporaryLinks)
        return TypedResources(contentType, contents)
    }

    private fun internalWriteContents(set: ResourceSet<ES>, typedResourcesApplying: TypedResourcesApplying<TC, EC>, timeWriting: LocalDateTime) {
        val contentType = typedResourcesApplying.contentType
        typedResourcesApplying.prelude().forEach {
            descriptorDaoHelper.applyContentsClient(set, contentType, it.action, it.contents, timeWriting)
        }
    }

    private fun internalWriteUpdate(updateResult: UpdateResult<TC, ES, EC>): ResourceSet<ES> {
        descriptorDaoHelper.applySet(updateResult.set)
        updateResult.typedResourcesUpdate.forEach {
            internalWriteUpdateContents(updateResult.set, it.contentType, it)
        }
        return updateResult.set
    }

    private fun internalWriteUpdateContents(set: ResourceSet<ES>, contentType: EC, typedResourcesUpdate: TypedResourcesUpdate<TC, EC>) {
        val links = typedResourcesUpdate.resourceContentsUn.map { it.link }
        val contents = typedResourcesUpdate.resourceContentsUn.map { it.content }
        descriptorDaoHelper.applyLinks(set.uid, contentType, links)
        descriptorDaoHelper.applyContents(contentType, contents)
    }

    private fun internalReadTemporary(uid: String, timeReading: LocalDateTime): TemporaryResult<TC, ES, EC> {
        val set = requireReadBase(uid)
        require(setHasTemporary(set)) { "set (${uid}) has no temporary data." }
        val typedResourcesTemp = descriptorDaoHelper.contentTypes()
            .map { contentType -> internalReadTemporaryContents(set, contentType) }
            .filter { it.resourceContentsTn.isNotEmpty() }
        return TemporaryResult(set, typedResourcesTemp, timeReading)
    }

    private fun internalReadTemporaryContents(set: ResourceSet<ES>, contentType: EC): TypedResourcesTemporary<TC, EC> {
        // first read temporary links
        val temporaryLinks = descriptorDaoHelper.readTemporaryLinks(set.uid, contentType)
        val contentsTn = descriptorDaoHelper.readContentsTn(contentType, temporaryLinks) { true }
        val newTemporaryLinks = temporaryLinks.map { it.copy(state = TemporaryState.Uploading) }
        descriptorDaoHelper.applyTemporaryLinks(set.uid, contentType, newTemporaryLinks)

        return TypedResourcesTemporary(contentType, contentsTn)
    }

    private fun internalAcceptTemporary(uid: String) {
        val set = requireReadBase(uid)
        require(setHasTemporary(set)) { "set (${uid}) has no temporary data." }
        descriptorDaoHelper.contentTypes().forEach {
            internalAcceptTemporaryContents(set, it)
        }
    }

    private fun internalAcceptTemporaryContents(set: ResourceSet<ES>, contentType: EC) {
        val temporaryLinks = descriptorDaoHelper.readTemporaryLinks(set.uid, contentType)
            .filter { it.state == TemporaryState.Uploading }
        descriptorDaoHelper.applyTemporaryLinks(set.uid, contentType, temporaryLinks.map { it.copy(state = TemporaryState.Accepted) })
    }


    private fun requireUserContext(): UserContext {
        require(userContext != UserContext.EMPTY) { "current userContext is EMPTY." }
        return userContext
    }

}