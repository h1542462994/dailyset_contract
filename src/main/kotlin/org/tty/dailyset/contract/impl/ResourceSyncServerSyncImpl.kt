package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.data.Relating
import org.tty.dailyset.contract.data.SnapshotResult
import org.tty.dailyset.contract.data.TypedResources
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.module.sync.ResourceSyncServerSync

class ResourceSyncServerSyncImpl<TS : ResourceSet<ES>, TL : ResourceLink<EC>, TC : ResourceContent, ES, EC>(
    private val descriptorSetSync: DescriptorSetSync<TS, TL, *, TC, *, ES, EC>
) : ResourceSyncServerSync<TS, TL, TC, ES, EC> {
    private val descriptorHelperSync = DaoHelperSync(descriptorSetSync)

    override fun readBase(uid: String): TS? {
        return descriptorHelperSync.readSet(uid)
    }

    override fun read(uid: String): SnapshotResult<TS, TC, ES, EC> {
        val set = requireNotNull(readBase(uid)) { "set(${uid} is not existed.)" }
        val typeResources = descriptorHelperSync.contentTypes()
            .map { contentType -> readContents(uid, contentType) }
            .filter { typedResources -> typedResources.resourceContents.isNotEmpty() }
        return SnapshotResult(
            set, typeResources
        )
    }

    override fun readContents(uid: String, contentType: EC): TypedResources<TC, EC> {
        require(readBase(uid) != null) { "set(${uid}) is not existed." }
        val links = descriptorHelperSync.readLinks(uid, contentType)
        val contents = descriptorHelperSync.readContents(links)
        return TypedResources(
            contentType = contentType,
            resourceContents = contents
        )
    }

    override fun write(result: SnapshotResult<TS, TC, ES, EC>): Relating<TS, ES> {
        TODO("Not yet implemented")
    }

    override fun writeContents(uid: String, contentType: EC, contents: List<TC>): Relating<TS, ES> {
        TODO("Not yet implemented")
    }

}