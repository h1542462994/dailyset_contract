package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.sync.ResourceLinkDaoCompatSync
import org.tty.dailyset.contract.declare.ResourceLink

interface ResourceLinkDescriptorSync<T: ResourceLink<EC>, TE, EC>: ResourceLinkDescriptor<T, TE, EC>, ResourceDescriptorSync  {
    val resourceLinkDaoCompatSync: ResourceLinkDaoCompatSync<EC, TE>
}