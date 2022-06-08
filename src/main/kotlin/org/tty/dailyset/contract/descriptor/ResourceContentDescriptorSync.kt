package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.sync.ResourceContentDaoCompatSync
import org.tty.dailyset.contract.declare.ResourceContent

sealed interface ResourceContentDescriptorSync<T: ResourceContent, TE, EC>: ResourceContentDescriptor<T, TE, EC> {
    val resourceContentDaoCompatSync: ResourceContentDaoCompatSync<TE>
}