package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.data.Relating
import org.tty.dailyset.contract.data.SnapshotResult
import org.tty.dailyset.contract.data.TypedResources
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet

interface ResourceSyncModuleSync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TC: ResourceContent, ES, EC> {
    fun readBase(uid: String): TS?
    fun read(uid: String): SnapshotResult<TS, TC, ES, EC>
    fun readContents(uid: String, contentType: EC): TypedResources<TC, EC>

}