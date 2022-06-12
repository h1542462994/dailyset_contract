package org.tty.dailyset.contract.module.async

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet

interface ResourceSyncServerAsync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TC: ResourceContent, ES, EC>:
    ResourceSyncModuleAsync<TS, TL, TC, ES, EC> {
}