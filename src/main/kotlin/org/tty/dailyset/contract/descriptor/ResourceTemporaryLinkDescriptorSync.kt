package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.sync.ResourceTemporaryLinkDaoCompatSync

interface ResourceTemporaryLinkDescriptorSync<TE: Any, EC>: ResourceTemporaryLinkDescriptor<TE, EC>, ResourceDescriptorSync {
    val resourceTemporaryLinkDaoCompatSync: ResourceTemporaryLinkDaoCompatSync<EC, TE>
}