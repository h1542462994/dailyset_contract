package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.module.sync.ResourceSyncServerSync

class ResourceSyncServerSyncImpl<TS : ResourceSet<ES>, TL : ResourceLink<EC>, TC : ResourceContent, ES, EC>(
    private val descriptorSetSync: DescriptorSetSync<TS, TL, *, TC, *, ES, EC>
) : ResourceSyncServerSync<TS, TL, TC, ES, EC> {
}