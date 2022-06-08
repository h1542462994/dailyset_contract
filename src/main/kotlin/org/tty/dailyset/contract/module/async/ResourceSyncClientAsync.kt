package org.tty.dailyset.contract.module.async

import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.declare.ResourceSetVisibility
import org.tty.dailyset.contract.declare.ResourceTemporalLink

interface ResourceSyncClientAsync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TV: ResourceSetVisibility, ES, EC>:
    ResourceSyncModuleAsync<TS, TL, ES, EC> {
}