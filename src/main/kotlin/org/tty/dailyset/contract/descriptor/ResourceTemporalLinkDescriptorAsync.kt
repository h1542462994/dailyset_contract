package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.async.ResourceTemporalLinkDaoCompatAsync
import org.tty.dailyset.contract.declare.ResourceTemporalLink

interface ResourceTemporalLinkDescriptorAsync<T: ResourceTemporalLink<EC>, TE, EC>: ResourceTemporalLinkDescriptor<T, TE, EC>, ResourceDescriptorAsync {
    val resourceTemporalLinkDaoCompatAsync: ResourceTemporalLinkDaoCompatAsync<EC, TE>
}