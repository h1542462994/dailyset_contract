package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.annotation.UseTransaction
import org.tty.dailyset.contract.annotation.UseUserContext
import org.tty.dailyset.contract.data.*
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet
import java.time.LocalDateTime

interface ResourceSyncClientSync<TC: ResourceContent, ES, EC>: ResourceSyncModuleSync<TC, ES, EC> {
    var userContext: UserContext

    @UseTransaction
    @UseUserContext
    fun writeUpdateAll(updateResults: List<UpdateResult<TC, ES, EC>>): List<ResourceSet<ES>>

    /**
     * write the **download** data from **server**.
     *
     *
     * @see [UpdateResult]
     * @see [ResourceSyncServerSync.readUpdate]
     */
    @UseTransaction
    fun writeUpdate(updateResult: UpdateResult<TC, ES, EC>): ResourceSet<ES>

    @UseUserContext
    @UseTransaction
    fun readTemporaryAll(timeReading: LocalDateTime): List<TemporaryResult<TC, ES, EC>>

    @UseTransaction
    fun readTemporaryAll(uids: List<String>, timeReading: LocalDateTime): List<TemporaryResult<TC, ES, EC>>

    @UseTransaction
    fun readTemporary(uid: String, timeReading: LocalDateTime): TemporaryResult<TC, ES, EC>

    @UseUserContext
    @UseTransaction
    fun acceptTemporaryAll()

    @UseTransaction
    fun acceptTemporaryAll(uids: List<String>)

    @UseTransaction
    fun acceptTemporary(uid: String)

    @UseUserContext
    fun readAvailableBases(): List<ResourceSet<ES>>

    @UseUserContext
    fun readTemporaryBases(): List<ResourceSet<ES>>

    @UseUserContext
    fun writeAvailableBases(sets: List<ResourceSet<ES>>)
}