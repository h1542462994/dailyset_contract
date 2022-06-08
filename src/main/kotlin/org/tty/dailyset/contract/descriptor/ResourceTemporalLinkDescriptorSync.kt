package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.sync.ResourceTemporalLinkDaoCompatSync
import org.tty.dailyset.contract.declare.ResourceTemporalLink

interface ResourceTemporalLinkDescriptorSync<T: ResourceTemporalLink<EC>, TE, EC>: ResourceTemporalLinkDescriptor<T, TE, EC>, ResourceDescriptorSync {
    val resourceTemporalLinkDaoCompatSync: ResourceTemporalLinkDaoCompatSync<EC, TE>
}