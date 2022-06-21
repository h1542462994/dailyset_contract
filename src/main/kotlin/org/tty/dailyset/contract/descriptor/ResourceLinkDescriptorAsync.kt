package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.async.ResourceLinkDaoCompatAsync
import org.tty.dailyset.contract.declare.ResourceLink

interface ResourceLinkDescriptorAsync<TE: Any, EC>: ResourceLinkDescriptor<TE, EC>, ResourceDescriptorAsync {
    val resourceLinkDaoCompatAsync: ResourceLinkDaoCompatAsync<EC, TE>
}