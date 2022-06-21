package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.async.ResourceTemporalLinkDaoCompatAsync

interface ResourceTemporalLinkDescriptorAsync<TE: Any, EC>: ResourceTemporalLinkDescriptor<TE, EC>, ResourceDescriptorAsync {
    val resourceTemporalLinkDaoCompatAsync: ResourceTemporalLinkDaoCompatAsync<EC, TE>
}