package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.bean.enums.InAction
import org.tty.dailyset.contract.bean.enums.RelatingOption
import org.tty.dailyset.contract.dao.sync.TransactionSupportSync
import org.tty.dailyset.contract.data.*
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.module.sync.ResourceSyncServerSync
import java.time.LocalDateTime

class ResourceSyncServerSyncImpl<TC : ResourceContent, ES, EC>(
    private val descriptorSet: DescriptorSetSync<TC, ES, EC>,
    private val transactionSupport: TransactionSupportSync?,
) : ResourceSyncServerSync<TC, ES, EC> {
    private val descriptorDaoHelper = DescriptorDaoHelperSync(descriptorSet)


    override fun readBase(uid: String): ResourceSet<ES>? {
        return descriptorDaoHelper.readSet(uid)
    }

    override fun read(uid: String): SnapshotResult<TC, ES, EC> {
        val set = requireNotNull(readBase(uid)) { "set(${uid} is not existed.)" }
        val typeResources = descriptorDaoHelper.contentTypes()
            .map { contentType -> readContents(uid, contentType) }
            .filter { typedResources -> typedResources.resourceContents.isNotEmpty() }
        return SnapshotResult(
            set, typeResources
        )
    }

    override fun readContents(uid: String, contentType: EC): TypedResources<TC, EC> {
        require(readBase(uid) != null) { "set(${uid}) is not existed." }
        val links = descriptorDaoHelper.readLinks(uid, contentType)
        val contents = descriptorDaoHelper.readContents(links)
        return TypedResources(
            contentType = contentType,
            resourceContents = contents
        )
    }

    override fun write(
        req: ApplyingReq<TC, EC>,
        timeWriting: LocalDateTime,
        relatingOption: RelatingOption
    ): Relating<ES> {
        val set = descriptorDaoHelper.readSet(req.setUid)
        require(set != null) { "set(${req.setUid} is not existed.)" }

        return inTransaction {
            val relatings = req.typedResourcesApplying.map {
                internalWriteContents(set, timeWriting, it, relatingOption)
            }
            val relating = combines(relatings)
            updateRelating(relating)
            relating
        }
    }



    override fun writeContents(
        uid: String,
        typedResourcesApplying: TypedResourcesApplying<TC, EC>,
        timeWriting: LocalDateTime,
        relatingOption: RelatingOption
    ): Relating<ES> {
        val set = descriptorDaoHelper.readSet(uid)
        require(set != null) { "set(${uid} is not existed.)" }
        return inTransaction {
            internalWriteContents(set, timeWriting, typedResourcesApplying, relatingOption)
        }
    }

    private fun <R> inTransaction(action: () -> R): R {
        var result: R? = null
        transactionSupport?.withTransaction {
            result = action()
        }
        return result!!
    }

    private fun updateRelating(relating: Relating<ES>): Relating<ES> {
        // write the value
        val newSets = relating.sets
            .map {
                val newSet = it.increaseVersionCopy()
                descriptorDaoHelper.applySet(newSet)
                newSet
            }

        return Relating(newSets)
    }

    private fun internalWriteContents(
        set: ResourceSet<ES>,
        timeWriting: LocalDateTime,
        typedResourcesApplying: TypedResourcesApplying<TC, EC>,
        relatingOption: RelatingOption
    ): Relating<ES> {
        val relatingSets = mutableSetOf<ResourceSet<ES>>()
        val contentType = typedResourcesApplying.contentType
        typedResourcesApplying.resourceContentsIn.forEach {
            when(it.action) {
                InAction.Single ->
                    relatingSets.addAll(descriptorDaoHelper.applyContentSingle(set, contentType, it.resourceContent!!, timeWriting, relatingOption))
                InAction.Apply -> TODO("Not implemented yet.")
                InAction.Remove -> TODO()
                InAction.RemoveAll -> TODO()
            }
        }
        return Relating(relatingSets.distinct())
    }

}