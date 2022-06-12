package org.tty.dailyset.contract.module.async

import org.tty.dailyset.contract.declare.*

interface ResourceSyncClientAsync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TC: ResourceContent, TV: ResourceSetVisibility, ES, EC>:
    ResourceSyncModuleAsync<TS, TL, TC, ES, EC> {
}