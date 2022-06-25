package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.async.ResourceTemporaryLinkDaoCompatAsync

interface ResourceTemporaryLinkDescriptorAsync<TE: Any, EC>: ResourceTemporaryLinkDescriptor<TE, EC>, ResourceDescriptorAsync {
    val resourceTemporaryLinkDaoCompatAsync: ResourceTemporaryLinkDaoCompatAsync<EC, TE>
}