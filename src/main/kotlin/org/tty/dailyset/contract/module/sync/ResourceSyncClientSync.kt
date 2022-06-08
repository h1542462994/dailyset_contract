package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.declare.ResourceSetVisibility
import org.tty.dailyset.contract.declare.ResourceTemporalLink

interface ResourceSyncClientSync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TV: ResourceSetVisibility, ES, EC>: ResourceSyncModuleSync<TS, TL, ES, EC> {
}