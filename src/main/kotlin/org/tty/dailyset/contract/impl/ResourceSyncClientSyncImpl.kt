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

    private fun requireReadBase(uid: String): ResourceSet<ES> {
        return requireNotNull(readBase(uid)) { "set(${uid} is not existed.)" }
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

    override fun writeUpdate(updateResult: UpdateResult<TC, ES, EC>): ResourceSet<ES> {
        TODO("Not yet implemented")
    }

    override fun writeUpdateContents(typedResourcesUpdate: TypedResourcesUpdate<TC, EC>): ResourceSet<ES> {
        TODO("Not yet implemented")
    }

    override fun readTemporal(resourceSet: ResourceSet<ES>, timeReading: LocalDateTime): TemporalResult<TC, ES, EC> {
        TODO("Not yet implemented")
    }

    override fun readTemporalContents(
        uid: String,
        contentType: EC,
        timeReading: LocalDateTime
    ): TypedResourcesTemp<TC, EC> {
        TODO("Not yet implemented")
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
            ProvideKeySelector { it.uid },
            ProvideKeySelector { it.uid }
        )

        val newVisibilities = mutableListOf<ResourceSetVisibility>()
        newVisibilities.addAll(diff.removeValues.map { it.copy(visible = false) })
        newVisibilities.addAll(diff.sameValues.map { it.sourceValue.copy(visible = true) })
        newVisibilities.addAll(diff.addValues.map { ResourceSetVisibility(uid = it.uid, userUid = userContext.userUid, visible = true) })
        descriptorDaoHelper.applySetVisibilities(newVisibilities)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R> inTransaction(action: () -> R): R {
        return transactionSupport.withTransactionR(action)
    }

    private fun internalReadContents(set: ResourceSet<ES>, contentType: EC): TypedResources<TC, EC> {
        TODO("Not yet implemented")
    }

    private fun internalWriteContents(set: ResourceSet<ES>, typedResourcesApplying: TypedResourcesApplying<TC, EC>, timeWriting: LocalDateTime) {
        val contentType = typedResourcesApplying.contentType
        typedResourcesApplying.prelude().forEach {
            descriptorDaoHelper.applyContentClient(set, contentType, it.action, it.contents, timeWriting)
        }
    }

    private fun requireUserContext(): UserContext {
        require(userContext != UserContext.EMPTY) { "current userContext is EMPTY." }
        return userContext
    }

}