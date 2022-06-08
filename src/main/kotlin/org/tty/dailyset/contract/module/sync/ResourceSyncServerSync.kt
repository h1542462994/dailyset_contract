package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet


interface ResourceSyncServerSync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, ES, EC>: ResourceSyncModuleSync<TS, TL, ES, EC> {
}