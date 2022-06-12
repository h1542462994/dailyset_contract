package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.data.SnapshotResult
import org.tty.dailyset.contract.data.TypedResources
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet

/**
 * universal apis about sync module.
 */
interface ResourceSyncModuleSync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TC: ResourceContent, ES, EC> {
    /**
     * read base info of [ResourceSet].
     * implementation is different between server and client.
     */
    fun readBase(uid: String): TS?

    /**
     * read a snapshot result of [ResourceSet].
     * implementation is different between server and client, it is combinations of [readContents].
     * in server, it is only depend on maintained data, but in client, maintained and local data will be related.
     * @see [SnapshotResult]
     * @throws IllegalArgumentException resource set of uid is not existed.
     */
    fun read(uid: String): SnapshotResult<TS, TC, ES, EC>

    /**
     * read a snapshot of certain [contentType].
     * implementation is different between server and client, it is combinations of [readContents].
     * in server, it is only depend on maintained data, but in client, maintained and local data will be related.
     * @throws IllegalArgumentException resource set of uid is not existed.
     */
    fun readContents(uid: String, contentType: EC): TypedResources<TC, EC>

}