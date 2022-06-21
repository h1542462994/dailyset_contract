package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.sync.ResourceLinkDaoCompatSync
import org.tty.dailyset.contract.declare.ResourceLink

interface ResourceLinkDescriptorSync<TE: Any, EC>: ResourceLinkDescriptor<TE, EC>, ResourceDescriptorSync  {
    val resourceLinkDaoCompatSync: ResourceLinkDaoCompatSync<EC, TE>
}