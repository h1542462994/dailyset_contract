package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.sync.ResourceSetVisibilityDaoCompatSync
import org.tty.dailyset.contract.declare.ResourceSetVisibility

interface ResourceSetVisibilityDescriptorSync<T: ResourceSetVisibility, TE>: ResourceSetVisibilityDescriptor<T, TE>, ResourceDescriptorSync {
    val resourceSetVisibilityDaoCompatSync: ResourceSetVisibilityDaoCompatSync<TE>
}