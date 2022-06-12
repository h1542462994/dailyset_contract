package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.data.Relating
import org.tty.dailyset.contract.data.SnapshotResult
import org.tty.dailyset.contract.data.TypedResources
import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.module.sync.ResourceSyncClientSync

class ResourceSyncClientSyncImpl<TS : ResourceSet<ES>, TL : ResourceLink<EC>, TTL : ResourceTemporalLink<EC>, TC : ResourceContent, TV : ResourceSetVisibility, ES, EC>(
    private val descriptorSetSync: DescriptorSetSync<TS, TL, TTL, TC, TV, ES, EC>
) : ResourceSyncClientSync<TS, TL, TTL, TC, TV, ES, EC> {

    override fun readBase(uid: String): TS? {
        TODO("Not yet implemented")
    }
    override fun read(uid: String): SnapshotResult<TS, TC, ES, EC> {
        TODO("Not yet implemented")
    }

    override fun readContents(uid: String, contentType: EC): TypedResources<TC, EC> {
        TODO("Not yet implemented")
    }

}