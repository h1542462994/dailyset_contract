package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.bean.annotation.UseTransaction
import org.tty.dailyset.contract.bean.annotation.UseUserContext
import org.tty.dailyset.contract.data.*
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet
import java.time.LocalDateTime

interface ResourceSyncClientSync<TC: ResourceContent, ES, EC>: ResourceSyncModuleSync<TC, ES, EC> {
    var userContext: UserContext

    /**
     * write the **download** data from **server**.
     *
     *
     * @see [UpdateResult]
     * @see [ResourceSyncServerSync.readUpdate]
     */
    @UseTransaction
    fun writeUpdate(updateResult: UpdateResult<TC, ES, EC>): ResourceSet<ES>

    /**
     * write the **download** data from **server** for certain contentType.
     *
     * @see [ResourceSyncServerSync.readUpdateContents]
     */
    @UseTransaction
    fun writeUpdateContents(typedResourcesUpdate: TypedResourcesUpdate<TC, EC>): ResourceSet<ES>


    fun readTemporal(resourceSet: ResourceSet<ES>, timeReading: LocalDateTime): TemporalResult<TC, ES, EC>

    fun readTemporalContents(uid: String, contentType: EC, timeReading: LocalDateTime): TypedResourcesTemp<TC, EC>


    @UseUserContext
    fun readAvailableBases(): List<ResourceSet<ES>>

    @UseUserContext
    fun writeAvailableBases(sets: List<ResourceSet<ES>>)
}