package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.data.TypedResourcesUpdate
import org.tty.dailyset.contract.data.UpdateResult
import org.tty.dailyset.contract.declare.*

interface ResourceSyncClientSync<TC: ResourceContent, ES, EC>: ResourceSyncModuleSync<TC, ES, EC> {
    fun writeUpdates(updateResult: UpdateResult<TC, ES, EC>): ResourceSet<ES>

    fun writeUpdateContents(typedResourcesUpdate: TypedResourcesUpdate<TC, EC>): ResourceSet<ES>
}