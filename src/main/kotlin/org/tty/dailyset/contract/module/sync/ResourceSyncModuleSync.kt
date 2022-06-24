package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.bean.annotation.UseTransaction
import org.tty.dailyset.contract.data.ApplyingReq
import org.tty.dailyset.contract.data.SnapshotResult
import org.tty.dailyset.contract.data.TypedResources
import org.tty.dailyset.contract.data.TypedResourcesApplying
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.declare.ResourceTemporalLink
import java.time.LocalDateTime

/**
 * universal apis about sync module.
 */
interface ResourceSyncModuleSync<TC: ResourceContent, ES, EC> {
    /**
     * read base info of [ResourceSet].
     *
     * implementation is different between server and client.
     */
    fun readBase(uid: String): ResourceSet<ES>?

    /**
     * read a snapshot result of [ResourceSet].
     *
     * it is combinations of [readContents].
     *
     * implementation is different between server and client.
     *
     * in **server**, it mean the storage data, because [ResourceTemporalLink] is not included.
     *
     * in **client**, it means the storage **download** or **temporal** data.
     *
     * @see [SnapshotResult]
     * @throws IllegalArgumentException resource set of uid is not existed.
     */
    fun read(uid: String): SnapshotResult<TC, ES, EC>

    /**
     * read a snapshot of certain [contentType].
     *
     * implementation is different between server and client.
     *
     * in **server**, it mean the storage data, because [ResourceTemporalLink] is not included.
     *
     * in **client**, it means the storage **download** or **temporal** data.
     * @throws IllegalArgumentException resource set of uid is not existed.
     */
    fun readContents(uid: String, contentType: EC): TypedResources<TC, EC>

    /**
     * create the set if absent.
     * @return the created set or existed set.
     */
    @UseTransaction
    fun createIfAbsent(set: ResourceSet<ES>): ResourceSet<ES>

    /**
     * write resource to sync module.
     *
     * implementation is different between server and client.
     *
     * in **server**, it means apply new data to sync module, and **increase version**.
     *
     * in **client**, it means apply temporal data to sync module, it will be used as the data **upload** to server.
     *
     * @see [ApplyingReq]
     * @see [org.tty.dailyset.contract.data.applyingReq]
     * @throws IllegalArgumentException resource set of uid is not existed.
     */
    @UseTransaction
    fun write(req: ApplyingReq<TC, EC>, timeWriting: LocalDateTime): ResourceSet<ES>

    /**
     * write certain resource of contentType to sync module.
     *
     * implementation is different between server and client.
     *
     * in **server**, it means apply new data to sync module, and **increase version**.
     *
     * in **client**, it means apply temporal data to sync module, it will be used as the data **upload** to server.
     * @throws IllegalArgumentException resource set of uid is not existed.
     */
    @UseTransaction
    fun writeContents(uid: String, typedResourcesApplying: TypedResourcesApplying<TC, EC>, timeWriting: LocalDateTime): ResourceSet<ES>

}