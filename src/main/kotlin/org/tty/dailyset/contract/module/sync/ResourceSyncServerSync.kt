package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.data.TypedResourcesUpdate
import org.tty.dailyset.contract.data.UpdateResult
import org.tty.dailyset.contract.declare.ResourceContent


interface ResourceSyncServerSync<TC: ResourceContent, ES, EC>: ResourceSyncModuleSync<TC, ES, EC> {
    fun readUpdate(uid: String, version: Int): UpdateResult<TC, ES, EC>

    fun readUpdateContents(uid: String, contentType: EC, version: Int): TypedResourcesUpdate<TC, EC>


}