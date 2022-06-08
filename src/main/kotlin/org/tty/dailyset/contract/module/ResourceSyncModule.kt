package org.tty.dailyset.contract.module

import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.declare.ResourceTemporalLink

interface ResourceSyncModule<ES, EC, TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>> {

}