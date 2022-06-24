package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.bean.enums.InAction
import org.tty.dailyset.contract.dao.sync.TransactionSupportSync
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
        val existedSet = readBase(set.uid)
        return if (existedSet != null) {
            existedSet
        } else {
            val newSet = set.copy(version = ResourceDefaults.VERSION_INIT)
            descriptorDaoHelper.applySet(newSet)
            newSet
        }
    }

    override fun write(
        req: ApplyingReq<TC, EC>,
        timeWriting: LocalDateTime,
    ): ResourceSet<ES> {
        val set = requireReadBase(req.setUid)
        return inTransaction {
            req.typedResourcesApplying.map {
                internalWriteContents(set, timeWriting, it)
            }
            updateResourceSet(set)
        }
    }

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

    @Suppress("UNCHECKED_CAST")
    private fun <R> inTransaction(action: () -> R): R {
        var result: R ?= null
        if (transactionSupport != null) {
            transactionSupport.withTransaction { result = action() }
        } else {
            result = action()
        }
        return result as R
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
        val contents = descriptorDaoHelper.readContents(links) { !it.isRemoved }
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
        val contentsUn = descriptorDaoHelper.readContentsUn(links) { true }
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

        val actionList = mutableListOf<Pair<InAction, MutableList<TC>>>()

        var replaceMode = false

        for(index in typedResourcesApplying.resourceContentsIn.indices) {
            val resourceContentIn = typedResourcesApplying.resourceContentsIn[index]
            if (resourceContentIn.action == InAction.Single) {
                require(actionList.isEmpty() && index == typedResourcesApplying.resourceContentsIn.size - 1) {
                    "other action is not supported in InAction.Single mode. or have multi elements."
                }
                actionList.add(Pair(InAction.Single, mutableListOf(resourceContentIn.resourceContent!!)))
            } else if (resourceContentIn.action == InAction.RemoveAll) {
                require(!replaceMode) { "other action is not supported in InAction.Replace mode." }
                actionList.clear()
                actionList.add(Pair(InAction.RemoveAll, mutableListOf()))
            } else if (resourceContentIn.action == InAction.Apply) {
                require(!replaceMode) { "other action is not supported in InAction.Replace mode." }
                if (actionList.isEmpty() || actionList.last().first != InAction.Apply) {
                    actionList.add(Pair(InAction.Apply, mutableListOf(resourceContentIn.resourceContent!!)))
                } else {
                    actionList.last().second.add(resourceContentIn.resourceContent!!)
                }
            } else if (resourceContentIn.action == InAction.Remove) {
                require(!replaceMode) { "other action is not supported in InAction.Replace mode." }
                if (actionList.isEmpty() || actionList.last().first != InAction.Remove) {
                    actionList.add(Pair(InAction.Remove, mutableListOf(resourceContentIn.resourceContent!!)))
                } else {
                    actionList.last().second.add(resourceContentIn.resourceContent!!)
                }
            } else if (resourceContentIn.action == InAction.Replace) {
                require(actionList.isEmpty() || actionList.last().first == InAction.Replace) {
                    "other action is not supported in InAction.Replace mode."
                }
                replaceMode = true
                if (actionList.isEmpty()) {
                    actionList.add(Pair(InAction.Replace, mutableListOf(resourceContentIn.resourceContent!!)))
                } else {
                    actionList.last().second.add(resourceContentIn.resourceContent!!)
                }
            }
        }

        actionList.forEach {
            when(it.first) {
                InAction.Single -> descriptorDaoHelper.applyContentSingle(set, contentType, it.second.first(), timeWriting)
                InAction.RemoveAll -> descriptorDaoHelper.applyContentRemoveAll(set, contentType, timeWriting)
                InAction.Apply -> descriptorDaoHelper.applyContentApply(set, contentType, it.second, timeWriting)
                InAction.Remove -> descriptorDaoHelper.applyContentRemove(set, contentType, it.second, timeWriting)
                InAction.Replace -> descriptorDaoHelper.applyContentReplace(set, contentType, it.second, timeWriting)
            }
        }

    }

}