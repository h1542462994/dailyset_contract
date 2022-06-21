package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.sync.ResourceSetVisibilityDaoCompatSync

interface ResourceSetVisibilityDescriptorSync<TE: Any>: ResourceSetVisibilityDescriptor<TE>, ResourceDescriptorSync {
    val resourceSetVisibilityDaoCompatSync: ResourceSetVisibilityDaoCompatSync<TE>
}