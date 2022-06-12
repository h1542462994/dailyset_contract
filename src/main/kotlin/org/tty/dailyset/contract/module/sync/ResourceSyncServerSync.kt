package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.data.ApplyingResult
import org.tty.dailyset.contract.data.Relating
import org.tty.dailyset.contract.data.TypedResourcesApplying
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet


interface ResourceSyncServerSync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TC: ResourceContent, ES, EC>: ResourceSyncModuleSync<TS, TL, TC, ES, EC> {

    /**
     * write a snapshot result to the sync module. it often used by writing server handled data.
     * if the written [ResourceSet] is not existed,
     */
    fun write(result: ApplyingResult<TS, TC, ES, EC>): Relating<TS, ES>

    fun writeContents(uid: String, contentType: EC, typedResourcesApplying: TypedResourcesApplying<TC, EC>): Relating<TS, ES>

}