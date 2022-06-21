package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.sync.ResourceTemporalLinkDaoCompatSync

interface ResourceTemporalLinkDescriptorSync<TE: Any, EC>: ResourceTemporalLinkDescriptor<TE, EC>, ResourceDescriptorSync {
    val resourceTemporalLinkDaoCompatSync: ResourceTemporalLinkDaoCompatSync<EC, TE>
}