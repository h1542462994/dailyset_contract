package org.tty.dailyset.contract.module.async

import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet

interface ResourceSyncServerAsync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, ES, EC>:
    ResourceSyncModuleAsync<TS, TL, ES, EC> {
}