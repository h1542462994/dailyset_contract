package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.async.ResourceLinkDaoCompatAsync
import org.tty.dailyset.contract.declare.ResourceLink

interface ResourceLinkDescriptorAsync<T: ResourceLink<EC>, TE, EC>: ResourceLinkDescriptor<T, TE, EC>, ResourceDescriptorAsync {
    val resourceLinkDaoCompatAsync: ResourceLinkDaoCompatAsync<EC, TE>
}