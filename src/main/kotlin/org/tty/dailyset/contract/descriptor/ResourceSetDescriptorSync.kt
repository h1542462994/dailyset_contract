package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.sync.ResourceSetDaoCompatSync

interface ResourceSetDescriptorSync<TE: Any, ES>: ResourceSetDescriptor<TE, ES>, ResourceDescriptorSync {
    val resourceSetDaoCompatSync: ResourceSetDaoCompatSync<TE>
}