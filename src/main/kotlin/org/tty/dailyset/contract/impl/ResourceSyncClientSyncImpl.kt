package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.data.SnapshotResult
import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.module.sync.ResourceSyncClientSync

class ResourceSyncClientSyncImpl<TS : ResourceSet<ES>, TL : ResourceLink<EC>, TTL : ResourceTemporalLink<EC>, TC : ResourceContent, TV : ResourceSetVisibility, ES, EC>(
    private val descriptorSetSync: DescriptorSetSync<TS, TL, TTL, TC, TV, ES, EC>
) : ResourceSyncClientSync<TS, TL, TTL, TC, TV, ES, EC> {
    override fun read(uid: String): SnapshotResult {
        TODO("Not yet implemented")
    }
}