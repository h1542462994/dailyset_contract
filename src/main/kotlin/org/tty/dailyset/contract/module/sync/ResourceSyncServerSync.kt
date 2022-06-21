package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.data.ApplyingReq
import org.tty.dailyset.contract.data.Relating
import org.tty.dailyset.contract.bean.enums.RelatingOption
import org.tty.dailyset.contract.data.TypedResourcesApplying
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet
import java.time.LocalDateTime


interface ResourceSyncServerSync<TC: ResourceContent, ES, EC>: ResourceSyncModuleSync<TC, ES, EC> {

    /**
     * write a snapshot result to the sync module. it often used by writing server handled data.
     * if the written [ResourceSet] is not existed,
     */
    fun write(req: ApplyingReq<TC, EC>, timeWriting: LocalDateTime, relatingOption: RelatingOption = RelatingOption.Separate): Relating<ES>

    fun writeContents(uid: String, typedResourcesApplying: TypedResourcesApplying<TC, EC>, timeWriting: LocalDateTime, relatingOption: RelatingOption = RelatingOption.Separate): Relating<ES>

}