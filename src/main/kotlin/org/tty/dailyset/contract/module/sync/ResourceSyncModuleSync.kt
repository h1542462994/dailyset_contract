package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.data.SnapshotResult
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet

interface ResourceSyncModuleSync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TC: ResourceContent, ES, EC> {
    fun read(uid: String): SnapshotResult
}