package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.declare.*

interface ResourceSyncClientSync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TC: ResourceContent, TV: ResourceSetVisibility, ES, EC>: ResourceSyncModuleSync<TS, TL, TC, ES, EC> {
}