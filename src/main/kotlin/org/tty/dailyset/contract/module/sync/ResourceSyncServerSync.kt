package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.annotation.UseTransaction
import org.tty.dailyset.contract.data.*
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
    fun readUpdateAll(sets: List<ResourceSet<ES>>): List<UpdateResult<TC, ES, EC>>

    /**
     * read the update result to **client**
     *
     * @see [UpdateResult]
     * @see [ResourceSyncClientSync.writeUpdate]
     * @throws IllegalArgumentException resource set of uid is not existed.
     */
    fun readUpdate(uid: String, version: Int): UpdateResult<TC, ES, EC>

    @UseTransaction
    fun writeTemporaryAll(temporaryResults: List<TemporaryResult<TC, ES, EC>>, timeWriting: LocalDateTime): List<ResourceSet<ES>>

    @UseTransaction
    fun writeTemporary(temporaryResult: TemporaryResult<TC, ES, EC>, timeWriting: LocalDateTime): ResourceSet<ES>
}