package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.data.TemporalResult
import org.tty.dailyset.contract.data.TypedResourcesTemp
import org.tty.dailyset.contract.data.TypedResourcesUpdate
import org.tty.dailyset.contract.data.UpdateResult
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet
import java.time.LocalDateTime


interface ResourceSyncServerSync<TC: ResourceContent, ES, EC>: ResourceSyncModuleSync<TC, ES, EC> {

    /**
     * read the update result to **client**
     *
     * @see [UpdateResult]
     * @see [ResourceSyncClientSync.writeUpdate]
     * @throws IllegalArgumentException resource set of uid is not existed.
     */
    fun readUpdate(uid: String, version: Int): UpdateResult<TC, ES, EC>

    /**
     * read the certain contentType update result to **client**
     *
     * @see [ResourceSyncClientSync.writeUpdateContents]
     * @throws IllegalArgumentException resource set of uid is not existed.
     */
    fun readUpdateContents(uid: String, contentType: EC, version: Int): TypedResourcesUpdate<TC, EC>

    fun writeTemporal(temporalResult: TemporalResult<TC, ES, EC>, timeWriting: LocalDateTime): ResourceSet<ES>

    fun writeTemporalContents(uid: String, typedResourcesTemp: TypedResourcesTemp<TC, EC>, timeWriting: LocalDateTime): ResourceSet<ES>
}