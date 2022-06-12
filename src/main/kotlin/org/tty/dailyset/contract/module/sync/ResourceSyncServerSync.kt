package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.data.Relating
import org.tty.dailyset.contract.data.SnapshotResult
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet


interface ResourceSyncServerSync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TC: ResourceContent, ES, EC>: ResourceSyncModuleSync<TS, TL, TC, ES, EC> {

    fun write(result: SnapshotResult<TS, TC, ES, EC>): Relating<TS, ES>

    fun writeContents(uid: String, contentType: EC, contents: List<TC>): Relating<TS, ES>
}